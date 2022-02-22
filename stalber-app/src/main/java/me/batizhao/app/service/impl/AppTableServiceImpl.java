package me.batizhao.app.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableCode;
import me.batizhao.app.domain.AppTableColumn;
import me.batizhao.app.mapper.AppTableMapper;
import me.batizhao.app.service.AppService;
import me.batizhao.app.service.AppTableService;
import me.batizhao.app.util.CodeGenUtils;
import me.batizhao.common.core.constant.GenConstants;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.StalberException;
import me.batizhao.common.core.util.FolderUtil;
import me.batizhao.dp.config.CodeProperties;
import me.batizhao.dp.config.GenConfig;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.service.CodeMetaService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 应用表接口实现类
 *
 * @author batizhao
 * @since 2022-01-27
 */
@Service
public class AppTableServiceImpl extends ServiceImpl<AppTableMapper, AppTable> implements AppTableService {

    @Autowired
    private AppTableMapper appTableMapper;
    @Autowired
    private AppService appService;
    @Autowired
    private CodeMetaService codeMetaService;
    @Autowired
    private CodeProperties codeProperties;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public IPage<AppTable> findAppTables(Page<AppTable> page, AppTable appTable) {
        LambdaQueryWrapper<AppTable> wrapper = Wrappers.lambdaQuery();
        if (appTable.getAppId() != null) {
            wrapper.eq(AppTable::getAppId, appTable.getAppId());
        }
        return appTableMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AppTable> findAppTables(AppTable appTable) {
        LambdaQueryWrapper<AppTable> wrapper = Wrappers.lambdaQuery();
        return appTableMapper.selectList(wrapper);
    }

    @SneakyThrows
    @Override
    public AppTable findById(Long id) {
        AppTable appTable = appTableMapper.selectById(id);

        if(appTable == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        // 初始化代码生成数据
        AppTableCode atc = new AppTableCode().setClassName(CodeGenUtils.columnToJava(appTable.getTableName()))
                .setClassComment(CodeGenUtils.replaceText(appTable.getTableComment()))
                .setClassAuthor(GenConfig.getAuthor())
                .setModuleName(GenConfig.getModuleName())
                .setPackageName(GenConfig.getPackageName())
                .setTemplate(GenConstants.TPL_CRUD);

        atc.setMappingPath(StringUtils.uncapitalize(atc.getClassName()));
        appTable.setCodeMetadata(objectMapper.writeValueAsString(atc));

        return appTable;
    }

    @Override
    @Transactional
    public AppTable saveOrUpdateAppTable(AppTable appTable) {
        if (appTable.getId() == null) {
            appTable.setCreateTime(LocalDateTime.now());
            appTable.setUpdateTime(LocalDateTime.now());
            appTableMapper.insert(appTable);
        } else {
            appTable.setUpdateTime(LocalDateTime.now());
            appTable.setStatus("nosync");
            appTableMapper.updateById(appTable);
        }
        return appTable;
    }

    @Override
    public Boolean syncTable(Long id) {
        AppTable appTable = findById(id);
        if (appTable.getStatus().equals("created")) {
            appService.syncCreateOrModifyTable(appTable, "create-table.vm", appTable.getDsName());
        } else {
            // app_table 表元数据
            JSONArray array = JSONUtil.parseArray(appTable.getColumnMetadata());
            List<AppTableColumn> appTableColumns = JSONUtil.toList(array, AppTableColumn.class);

            // 数据库表元数据
            List<CodeMeta> dbTableColumns = codeMetaService.findColumnsByTableName(appTable.getTableName(), appTable.getDsName());
            if (dbTableColumns.isEmpty()) {
                throw new StalberException(String.format("Table '%s.%s' doesn't exist 。", appTable.getDsName(), appTable.getTableName()));
            }
            List<String> dbTableColumnNames = dbTableColumns.stream().map(CodeMeta::getColumnName).collect(Collectors.toList());

            // 增加数据库不存在的列
            List<AppTableColumn> appTableAddColumns = new ArrayList<>();
            appTableColumns.forEach(column -> {
                if (!dbTableColumnNames.contains(column.getName())) {
                    appTableAddColumns.add(column);
                }
            });
            if (!appTableAddColumns.isEmpty()) {
                appService.syncAlterTable(appTable.getTableName(), appTableAddColumns, appTable.getDsName());
            }

            // 删除数据库不存在的列
            List<String> appTableColumnNames = appTableColumns.stream().map(AppTableColumn::getName).collect(Collectors.toList());
            List<String> delColumns = dbTableColumnNames.stream().filter(column -> !appTableColumnNames.contains(column)).collect(Collectors.toList());
            if (!delColumns.isEmpty()) {
                appService.syncDropTable(appTable.getTableName(), delColumns, appTable.getDsName());
            }

            // 修改列属性
            appService.syncCreateOrModifyTable(appTable, "modify-table.vm", appTable.getDsName());
        }

        appTable.setUpdateTime(LocalDateTime.now());
        appTable.setStatus("synced");
        appTableMapper.updateById(appTable);
        return true;
    }

    @SneakyThrows
    private AppTable prepareCodeMeta(Long id) {
        AppTable appTable = findById(id);

        if(StringUtils.isBlank(appTable.getCodeMetadata())) {
            throw new StalberException("没有配置生成信息！");
        }

        JSONArray array = JSONUtil.parseArray(appTable.getColumnMetadata());
        List<AppTableColumn> appTableColumns = JSONUtil.toList(array, AppTableColumn.class);

        appTableColumns.forEach(CodeGenUtils::initColumnField);
        appTable.setColumnMetadata(objectMapper.writeValueAsString(appTableColumns));
        return appTable;
    }

    @Override
    public byte[] downloadCode(Long id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generateCode(prepareCodeMeta(id), zip);
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码然后下载
     */
    @SneakyThrows
    private void generateCode(AppTable appTable, ZipOutputStream zip) {
        // 封装模板数据
        Map<String, Object> map = CodeGenUtils.prepareContext(appTable);
        String templatePath = Paths.get(".", codeProperties.getTemplateUrl()).toAbsolutePath().toString();
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(templatePath, TemplateConfig.ResourceMode.FILE));

        // 获取模板列表
        for (Path path : FolderUtil.build(templatePath)) {
            if (StringUtils.containsAny(path.toString(), "common", "commons")) continue;

            File file = path.toFile();
            String filePath = file.getPath().replace(templatePath, "");
            if (CodeGenUtils.getFileName(appTable, filePath) == null) continue;

            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = engine.getTemplate(filePath);
            tpl.render(map, sw);

            // 添加到zip
//            zip.putNextEntry(new ZipEntry(filePath.substring(0, filePath.lastIndexOf("."))));
            zip.putNextEntry(new ZipEntry(Objects.requireNonNull(CodeGenUtils.getFileName(appTable, filePath))));
            IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
            IoUtil.close(sw);
            zip.closeEntry();
        }
    }

}
