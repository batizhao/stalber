package me.batizhao.dp.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.config.FileProperties;
import me.batizhao.common.constant.GenConstants;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.common.exception.StalberException;
import me.batizhao.common.util.FolderUtil;
import me.batizhao.dp.config.GenConfig;
import me.batizhao.dp.domain.*;
import me.batizhao.dp.mapper.CodeMapper;
import me.batizhao.dp.service.CodeMetaService;
import me.batizhao.dp.service.CodeService;
import me.batizhao.dp.service.CodeTemplateService;
import me.batizhao.dp.service.FormService;
import me.batizhao.dp.util.CodeGenUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 在这里要注意，在事务开启的情况下，动态数据源会无效。
 * 无论是上级方法，还是当前方法，都不能开启事务。
 *
 * @author batizhao
 * @date 2020/10/10
 */
@Service
@Slf4j
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements CodeService {

    @Autowired
    private CodeMapper codeMapper;
    @Autowired
    private CodeMetaService codeMetaService;
    @Autowired
    private FormService formService;
    @Autowired
    private CodeTemplateService codeTemplateService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FileProperties fileProperties;

    @Override
    public IPage<Code> findCodes(Page<Code> page, Code code) {
        LambdaQueryWrapper<Code> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(code.getTableName())) {
            wrapper.like(Code::getTableName, code.getTableName());
        }
        return codeMapper.selectPage(page, wrapper);
    }

    @Override
    public Code findById(Long id) {
        Code code = codeMapper.selectById(id);

        if (code == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return code;
    }

    /**
     * 当前方法开启事务，会导致动态数据源无法切换。
     * 为了解决这个问题，单独封装了 saveCode 方法处理事务。
     * 但是这种方式，只适合这个方法，并不适合所有的事务+动态数据源的场景。
     *
     * @param code 生成代码
     * @return
     * @see <a href="SpringBoot+Mybatis配置多数据源及事务方案">https://juejin.cn/post/6844904159074844685</a>
     */
    @Override
    public Code saveOrUpdateCode(Code code) {
        if (code.getId() == null) {
            List<CodeMeta> codeMetas = codeMetaService.findColumnsByTableName(code.getTableName(), code.getDsName());
            saveCode(code, codeMetas);
        } else {
            updateCode(code);
        }

        return code;
    }

    @Override
    @Transactional
    public Code saveCode(Code code, List<CodeMeta> codeMetas) {
//        FormMarker fm = new FormMarker();
//        List<Element> elements = new ArrayList<>();
//
//        // 先初始化元数据
//        codeMetas.forEach(cm -> {
//            CodeGenUtils.initColumnField(cm);
//
//            if ((cm.getSave() != null && cm.getSave()) || cm.getPrimaryKey()) {
//                Element element = new Element();
//                element.setType(cm.getHtmlType());
//                element.setName(cm.getColumnComment());
//                element.setModel(cm.getColumnName());
//                element.setKey(IdUtil.objectId());
//                element.setIcon("icon-" + cm.getHtmlType());
//
//                if (cm.getHtmlType() != null && cm.getHtmlType().equals(GenConstants.HTML_SELECT)) {
//                    element.setOptions(new SelectOptions());
//                } else if (cm.getHtmlType() != null && (cm.getHtmlType().equals(GenConstants.HTML_CHECKBOX)
//                        || cm.getHtmlType().equals(GenConstants.HTML_RADIO))) {
//                    element.setOptions(new RadioAndCheckboxOptions());
//                } else if (cm.getPrimaryKey()) {
//                    element.setOptions(new Options(true));
//                } else {
//                    Options options = new Options();
//                    if (cm.getRequired() != null && cm.getRequired()) {
//                        options.setRequired(true);
//                        options.setHidden(false);
//                    }
//                    element.setOptions(options);
//                }
//
//                if (cm.getRequired() != null && cm.getRequired()) {
//                    List<Rules> rules = new ArrayList<>();
//                    rules.add(new Rules(true, "Required"));
//                    element.setRules(rules);
//                }
//
//                elements.add(element);
//            }
//        });
//
//        //设置 FormMarker 元素
//        fm.setList(elements);

        FormGenerator fg = new FormGenerator();
        List<Fields> fields = new ArrayList<>();

        int[] i = {100};
        codeMetas.forEach(cm -> {
            CodeGenUtils.initColumnField(cm);
            if (cm.getSave() != null && cm.getSave() && cm.getHtmlType() != null) {
                Fields field = new Fields();
                field.setVModel(cm.getColumnName())
                        .setPlaceholder("请输入" + cm.getColumnComment());

                Style style = new Style();
                field.setStyle(style);

                switch (cm.getHtmlType()) {
                    case GenConstants.HTML_TEXTAREA:
                        Config config = new Config(cm.getColumnComment(),
                                "el-input",
                                cm.getHtmlType(),
                                cm.getRequired() != null && cm.getRequired(),
                                i[0]++,
                                RandomUtil.randomNumbers(17));

                        field.setType(GenConstants.HTML_TEXTAREA)
                                .setAutosize(new Autosize())
                                .setReadonly(false)
                                .setShowWordLimit(false)
                                .setConfig(config);

                        break;
                    case GenConstants.HTML_SELECT:
                        config = new Config(cm.getColumnComment(),
                                "el-select",
                                cm.getHtmlType(),
                                cm.getRequired() != null && cm.getRequired(),
                                i[0]++,
                                RandomUtil.randomNumbers(17));

                        field.setSlot(new SlotList())
                                .setMultiple(false)
                                .setFilterable(false)
                                .setClearable(true)
                                .setConfig(config);

                        break;
//                    case GenConstants.HTML_RADIO:
//                        config = new Config(cm.getColumnComment(),
//                                "el-radio-group",
//                                cm.getHtmlType(),
//                                cm.getRequired() != null && cm.getRequired(),
//                                i[0]++,
//                                RandomUtils.nextInt(),
//                                "default",
//                                false);
//
//                        field.setType(GenConstants.HTML_RADIO)
//                                .setSlot(new SlotList())
//                                .setSize("medium")
//                                .setConfig(config);
//
//                        break;
//                    case GenConstants.HTML_CHECKBOX:
//                        config = new CheckboxConfig(cm.getColumnComment(),
//                                "el-checkbox-group",
//                                cm.getHtmlType(),
//                                cm.getRequired() != null && cm.getRequired(),
//                                i[0]++,
//                                RandomUtils.nextInt(),
//                                "default",
//                                false,
//                                new ArrayList<>());
//
//                        field.setType(GenConstants.HTML_CHECKBOX)
//                                .setSlot(new SlotList())
//                                .setSize("medium")
//                                .setConfig(config);
//
//                        break;
//                    case GenConstants.HTML_SWITCH:
//                        config = new SwitchConfig(cm.getColumnComment(),
//                                "el-switch",
//                                cm.getHtmlType(),
//                                cm.getRequired() != null && cm.getRequired(),
//                                i[0]++,
//                                RandomUtils.nextInt(),
//                                "default",
//                                false,
//                                "open");
//
//                        field.setActiveText("")
//                                .setInactiveText("")
//                                .setActiveValue("open")
//                                .setInactiveValue("close")
//                                .setConfig(config);
//                        break;
                    default:
                        config = new Config(cm.getColumnComment(),
                                "el-input",
                                cm.getHtmlType(),
                                cm.getRequired() != null && cm.getRequired(),
                                i[0]++,
                                RandomUtil.randomNumbers(17));

                        field.setSlot(new InputSlot())
                                .setConfig(config)
                                .setClearable(true)
                                .setReadonly(false)
                                .setShowWordLimit(false)
                                .setPrefixIcon("")
                                .setSuffixIcon("");
                        break;
                }

                fields.add(field);
            }
        });

        fg.setFields(fields);

        try {
            Form form = new Form()
                    .setName(code.getDsName() + ":" + code.getTableName() + ":" + RandomUtil.randomString(5))
                    .setDescription(code.getClassComment())
                    .setMetadata(objectMapper.writeValueAsString(fg));
            form = formService.saveOrUpdateForm(form);
            code.setFormKey(form.getFormKey());
        } catch (JsonProcessingException e) {
            log.error("Serialization failed，{}", e.getMessage());
            throw new StalberException("Serialization failed！", e);
        }

        code.setCreateTime(LocalDateTime.now());
        code.setUpdateTime(LocalDateTime.now());
        this.save(code);
        codeMetas.forEach(cm -> cm.setCodeId(code.getId()));
        codeMetaService.saveBatch(codeMetas);

        return code;
    }

    @Override
    @Transactional
    public Code updateCode(Code code) {
        code.setUpdateTime(LocalDateTime.now());
        this.updateById(code);
        if (!CollectionUtils.isEmpty(code.getCodeMetaList())) {
            codeMetaService.updateBatchById(code.getCodeMetaList());
        }

        return code;
    }

    @Override
    @Transactional
    public Boolean deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
        ids.forEach(i -> codeMetaService.remove(Wrappers.<CodeMeta>lambdaQuery().eq(CodeMeta::getCodeId, i)));
        return true;
    }

    @Override
    @DS("#last")
    public IPage<Code> findTables(Page<Code> page, Code code, String dsName) {
        IPage<Code> p = codeMapper.selectTablePageByDs(page, code);
        List<Code> c = p.getRecords();

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
     * @param codes
     * @return
     */
    @Override
    public Boolean importTables(List<Code> codes) {
        if (codes == null) return false;
        for (Code c : codes) {
            CodeGenUtils.initData(c);
            saveOrUpdateCode(c);
        }
        return true;
    }

    @Override
    public byte[] downloadCode(List<Long> ids) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        for (Long i : ids) {
            generateCode(prepareCodeMeta(i), zip);
        }

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
    public Boolean syncCodeMeta(Long id) {
        Code code = this.findById(id);
        List<CodeMeta> codeMetas = codeMetaService.findByCodeId(code.getId());
        List<CodeMeta> dbTableColumns = codeMetaService.findColumnsByTableName(code.getTableName(), code.getDsName());
        return syncColumn(id, codeMetas, dbTableColumns);
    }

    @Override
    @Transactional
    public Boolean syncColumn(Long id, List<CodeMeta> codeMetas, List<CodeMeta> dbTableColumns) {
        if (CollectionUtils.isEmpty(dbTableColumns)) {
            throw new StalberException("Failed to synchronize data, original table structure does not exist!");
        }

        List<String> tableColumnNames = codeMetas.stream().map(CodeMeta::getColumnName).collect(Collectors.toList());

        List<String> dbTableColumnNames = dbTableColumns.stream().map(CodeMeta::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            if (!tableColumnNames.contains(column.getColumnName())) {
                column.setCodeId(id);
                CodeGenUtils.initColumnField(column);
                codeMetaService.save(column);
            }
        });

        List<CodeMeta> delColumns = codeMetas.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(delColumns)) {
            List<Long> ids = delColumns.stream().map(CodeMeta::getId).collect(Collectors.toList());
            codeMetaService.removeByIds(ids);
        }

        return true;
    }

    private Code prepareCodeMeta(Long id) {
        Code code = findById(id);
        List<CodeMeta> codeMetas = codeMetaService.findByCodeId(code.getId());
        code.setCodeMetaList(codeMetas);

        if (code.getTemplate().equals(GenConstants.TPL_ONE_TO_MANY) && code.getSubTableId() != null) {
            Code subCode = findById(code.getSubTableId());
//            subCode.setCodeMetaList(codeMetaService.findByCodeId(subCode.getId()));
            code.setSubCode(subCode);
        }

        code.setRelationCode(codeMapper.selectList(Wrappers.<Code>query().lambda().eq(Code::getSubTableId, code.getId())));
        return code;
    }

    /**
     * 预览代码
     */
    @SneakyThrows
    private Map<String, String> previewCode(Code code) {
        Map<String, Object> map = CodeGenUtils.prepareContext(code);
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(fileProperties.getCodeTemplateLocation(), TemplateConfig.ResourceMode.FILE));

        Map<String, String> dataMap = new LinkedHashMap<>();
        // 获取模板列表
        for (Path path : FolderUtil.build(fileProperties.getCodeTemplateLocation())) {
            if (StringUtils.containsAny(path.toString(), "common", "commons")) continue;

            File file = path.toFile();
            String filePath = file.getPath().replace(fileProperties.getCodeTemplateLocation(), "");

            if (code.getTemplate().equals(GenConstants.TPL_TREE)) {
                if (filePath.contains("index.vue")) {
                    continue;
                }
            } else {
                if (filePath.contains("index-tree.vue")) {
                    continue;
                }
            }

            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = engine.getTemplate(filePath);
            tpl.render(map, sw);
            String filename = file.getName();
            dataMap.put(filename.substring(0, filename.lastIndexOf(".")), sw.toString());
        }
        return dataMap;
    }

    /**
     * 生成代码然后下载
     */
    @SneakyThrows
    private void generateCode(Code code, ZipOutputStream zip) {
        // 封装模板数据
        Map<String, Object> map = CodeGenUtils.prepareContext(code);

        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(fileProperties.getCodeTemplateLocation(), TemplateConfig.ResourceMode.FILE));

        // 获取模板列表
        for (Path path : FolderUtil.build(fileProperties.getCodeTemplateLocation())) {
            if (StringUtils.containsAny(path.toString(), "common", "commons")) continue;

            File file = path.toFile();
            String filePath = file.getPath().replace(fileProperties.getCodeTemplateLocation(), "");
            if (CodeGenUtils.getFileName(code, filePath) == null) continue;

            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = engine.getTemplate(filePath);
            tpl.render(map, sw);

            // 添加到zip
//            zip.putNextEntry(new ZipEntry(filePath.substring(0, filePath.lastIndexOf("."))));
            zip.putNextEntry(new ZipEntry(Objects.requireNonNull(CodeGenUtils.getFileName(code, filePath))));
            IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString());
            IoUtil.close(sw);
            zip.closeEntry();
        }
    }

    /**
     * 生成代码到路径
     */
    @SneakyThrows
    private void generateCode(Code code) {
        // 封装模板数据
        Map<String, Object> map = CodeGenUtils.prepareContext(code);

        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig(fileProperties.getCodeTemplateLocation(), TemplateConfig.ResourceMode.FILE));

        // 获取模板列表
        for (Path path : FolderUtil.build(fileProperties.getCodeTemplateLocation())) {
            if (StringUtils.containsAny(path.toString(), "common", "commons")) continue;

            File file = path.toFile();
            String filePath = file.getPath().replace(fileProperties.getCodeTemplateLocation(), "");
            if (CodeGenUtils.getFileName(code, filePath) == null) continue;

            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = engine.getTemplate(filePath);
            tpl.render(map, sw);
            filePath = filePath.substring(0, filePath.lastIndexOf("."));
            try {
                String genPath = getGenPath(code, filePath);
                if (StringUtils.containsAny(filePath, ".js", ".vue")) {
                    genPath = getGenFrontPath(code, filePath);
                }
                FileUtils.writeStringToFile(new File(genPath), sw.toString(), CharsetUtil.UTF_8);
            } catch (IOException e) {
                throw new StalberException("渲染模板失败，表名：" + code.getTableName());
            }
        }
    }

    /**
     * 获取代码生成地址
     *
     * @param code
     * @param filePath
     * @return 生成地址
     */
    private String getGenPath(Code code, String filePath) {
        String genPath = code.getPath();
        if (StringUtils.equals(genPath, "/")) {
            return System.getProperty("user.dir") + File.separator + CodeGenUtils.getFileName(code, filePath);
        }
        return genPath + File.separator + CodeGenUtils.getFileName(code, filePath);
    }

    /**
     * 获取前端代码生成地址
     *
     * @param code
     * @param filePath
     * @return 生成地址
     */
    private String getGenFrontPath(Code code, String filePath) {
        String genPath = code.getFrontPath();
        if (StringUtils.equals(genPath, "/")) {
            return System.getProperty("user.dir") + File.separator + CodeGenUtils.getFileName(code, filePath);
        }
        return genPath + File.separator + CodeGenUtils.getFileName(code, filePath);
    }
}
