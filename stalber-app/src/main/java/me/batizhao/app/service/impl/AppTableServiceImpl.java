package me.batizhao.app.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import me.batizhao.common.core.config.CodeProperties;
import me.batizhao.app.config.GenConfig;
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
import java.util.*;
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

    @Override
    public AppTable findById(Long id) {
        AppTable appTable = appTableMapper.selectById(id);

        if(appTable == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

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

    @SneakyThrows
    @Override
    public Boolean updateCodeMetadataById(AppTable appTable) {
        //初始化 ColumnMetadata 属性
        JSONArray array = JSONUtil.parseArray(appTable.getColumnMetadata());
        List<AppTableColumn> appTableColumns = JSONUtil.toList(array, AppTableColumn.class);
        appTableColumns.forEach(CodeGenUtils::initColumnField);

        LambdaUpdateWrapper<AppTable> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AppTable::getId, appTable.getId())
                .set(AppTable::getCodeMetadata, appTable.getCodeMetadata())
                .set(AppTable::getColumnMetadata, objectMapper.writeValueAsString(appTableColumns));

        return appTableMapper.update(null, wrapper) == 1;
    }

    @SneakyThrows
    @Override
    public Boolean syncTable(Long id) {
        AppTable appTable = findById(id);

        if (appTable.getStatus().equals("created")) {
            appService.syncCreateOrModifyTable(appTable, "create-table.vm", appTable.getDsName());

            initCodeMetadata(appTable);
        } else {
            // 数据库表元数据
            List<AppTableColumn> dbTableColumns = findColumnsByTableName(appTable.getTableName(), appTable.getDsName());
            if (dbTableColumns.isEmpty()) {
                appService.syncCreateOrModifyTable(appTable, "create-table.vm", appTable.getDsName());
            }
            List<String> dbTableColumnNames = dbTableColumns.stream().map(AppTableColumn::getName).collect(Collectors.toList());

            // app_table 表元数据
            JSONArray array = JSONUtil.parseArray(appTable.getColumnMetadata());
            List<AppTableColumn> appTableColumns = JSONUtil.toList(array, AppTableColumn.class);

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

    @Override
    public byte[] downloadCode(Long id) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generateCode(prepareCodeMeta(id), zip);
        IoUtil.close(zip);
        return outputStream.toByteArray();
    }

    @Override
    public Boolean generateCode(Long id) {
        generateCode(prepareCodeMeta(id));
        return true;
    }

    @Override
    public Map<String, String> previewCode(Long id) {
        return previewCode(prepareCodeMeta(id));
    }

    @Override
    @DS("#last")
    public IPage<AppTable> findTables(Page<AppTable> page, AppTable appTable, String dsName) {
        IPage<AppTable> p = appTableMapper.selectTablePageByDs(page, appTable);
        List<AppTable> c = p.getRecords();

        if (StringUtils.isBlank(dsName)) {
            dsName = "master";
        }

        String finalDsName = dsName;
        c.forEach(ll -> ll.setDsName(finalDsName));

        return p;
    }

    /**
     * 这里不能开启事务，会导致动态数据源失效。
     *
     * @param appTables
     * @return
     */
    @SneakyThrows
    @Override
    public Boolean importTables(List<AppTable> appTables) {
        if (appTables == null) return false;
        for (AppTable appTable : appTables) {
            initCodeMetadata(appTable);
            // 初始化 columnMetadata
            List<AppTableColumn> appTableColumns = findColumnsByTableName(appTable.getTableName(), appTable.getDsName());
            appTable.setColumnMetadata(objectMapper.writeValueAsString(appTableColumns));

            appTable.setCreateTime(LocalDateTime.now());
            appTable.setUpdateTime(LocalDateTime.now());
            appTable.setStatus("synced");
            appTableMapper.insert(appTable);
        }
        return true;
    }

    @Override
    @DS("#last")
    public List<AppTableColumn> findColumnsByTableName(String tableName, String dsName) {
        return appTableMapper.selectColumnsByTableName(tableName);
    }

    @Override
    public List<AppTable> listTableRelations(Long id) {
        AppTable appTable = appTableMapper.selectById(id);

        LambdaQueryWrapper<AppTable> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(AppTable::getAppId, appTable.getAppId());
        wrapper.eq(AppTable::getDsName, appTable.getDsName());
        wrapper.ne(AppTable::getId, id);

        return appTableMapper.selectList(wrapper);
    }

    /**
     * 初始化 codeMetadata
     * @param appTable
     */
    @SneakyThrows
    private void initCodeMetadata(AppTable appTable) {
        // 初始化代码生成数据
        AppTableCode atc = new AppTableCode().setClassName(CodeGenUtils.columnToJava(appTable.getTableName()))
                .setClassComment(CodeGenUtils.replaceText(appTable.getTableComment()))
                .setClassAuthor(GenConfig.getAuthor())
                .setModuleName(GenConfig.getModuleName())
                .setPackageName(GenConfig.getPackageName())
                .setTemplate(GenConstants.TPL_SINGLE);

        atc.setMappingPath(StringUtils.uncapitalize(atc.getClassName()));
        appTable.setCodeMetadata(objectMapper.writeValueAsString(atc));
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

    /**
     * 生成代码到路径
     */
    @SneakyThrows
    private void generateCode(AppTable appTable) {
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
            filePath = filePath.substring(0, filePath.lastIndexOf("."));
            String genPath = getGenPath(appTable, filePath);
            FileUtil.writeString(sw.toString(), new File(genPath), CharsetUtil.UTF_8);
        }
    }

    /**
     * 获取代码生成地址
     *
     * @param appTable
     * @param filePath
     * @return 生成地址
     */
    private String getGenPath(AppTable appTable, String filePath) {
        AppTableCode appTableCode = JSONUtil.toBean(appTable.getCodeMetadata(), AppTableCode.class);
        String genPath = appTableCode.getPath();
        if (StringUtils.equals(genPath, "/")) {
            return System.getProperty("user.dir") + File.separator + CodeGenUtils.getFileName(appTable, filePath);
        }
        return genPath + File.separator + CodeGenUtils.getFileName(appTable, filePath);
    }

    /**
     * 预览代码
     */
    @SneakyThrows
    private Map<String, String> previewCode(AppTable appTable) {
        Map<String, Object> map = CodeGenUtils.prepareContext(appTable);
        String templatePath = Paths.get(".", codeProperties.getTemplateUrl()).toAbsolutePath().toString();
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(templatePath, TemplateConfig.ResourceMode.FILE));

        Map<String, String> dataMap = new LinkedHashMap<>();
        // 获取模板列表
        for (Path path : FolderUtil.build(templatePath)) {
            if (StringUtils.containsAny(path.toString(), "common", "commons")) continue;

            File file = path.toFile();
            String filePath = file.getPath().replace(templatePath, "");

            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = engine.getTemplate(filePath);
            tpl.render(map, sw);
            String filename = file.getName();
            dataMap.put(filename.substring(0, filename.lastIndexOf(".")), sw.toString());
        }
        return dataMap;
    }

}
