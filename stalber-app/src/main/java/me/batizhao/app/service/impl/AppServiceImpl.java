package me.batizhao.app.service.impl;

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
import lombok.SneakyThrows;
import me.batizhao.app.domain.App;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableColumn;
import me.batizhao.app.mapper.AppMapper;
import me.batizhao.app.service.AppService;
import me.batizhao.common.core.exception.NotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用接口实现类
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Autowired
    private AppMapper appMapper;

    @Override
    public IPage<App> findApps(Page<App> page, App app) {
        LambdaQueryWrapper<App> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(app.getName())) {
            wrapper.like(App::getName, app.getName());
        }
        if (app.getTypeId() != null) {
            wrapper.eq(App::getTypeId, app.getTypeId());
        }
        return appMapper.selectPage(page, wrapper);
    }

    @Override
    public List<App> findApps(App app) {
        LambdaQueryWrapper<App> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(app.getName())) {
            wrapper.like(App::getName, app.getName());
        }
        return appMapper.selectList(wrapper);
    }

    @Override
    public App findById(Long id) {
        App app = appMapper.selectById(id);

        if(app == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return app;
    }

    @Override
    @Transactional
    public App saveOrUpdateApp(App app) {
        if (app.getId() == null) {
            app.setCreateTime(LocalDateTime.now());
            app.setUpdateTime(LocalDateTime.now());
            appMapper.insert(app);
        } else {
            app.setUpdateTime(LocalDateTime.now());
            appMapper.updateById(app);
        }

        return app;
    }

    @Override
    @Transactional
    public Boolean updateStatus(App app) {
        LambdaUpdateWrapper<App> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(App::getId, app.getId()).set(App::getStatus, app.getStatus());
        return appMapper.update(null, wrapper) == 1;
    }

    @Override
    @DS("#last")
    public Boolean syncCreateOrModifyTable(AppTable appTable, String template, String dsName) {
        Map<String, Object> sqlParamMap = new HashMap<>();
        sqlParamMap.put("tableName", appTable.getTableName());
        sqlParamMap.put("tableComment", appTable.getTableComment());

        JSONArray array = JSONUtil.parseArray(appTable.getColumnMetadata());
        List<AppTableColumn> appTableColumns = JSONUtil.toList(array, AppTableColumn.class);
        sqlParamMap.put("columns", appTableColumns);

        return appMapper.createTable(writer(sqlParamMap, template)) >= 0;
    }

    @Override
    @DS("#last")
    public Boolean syncAlterTable(String tableName, List<AppTableColumn> appTableColumns, String dsName) {
        Map<String, Object> sqlParamMap = new HashMap<>();
        sqlParamMap.put("tableName", tableName);
        sqlParamMap.put("columns", appTableColumns);

        return appMapper.createTable(writer(sqlParamMap, "alter-table.vm")) >= 0;
    }

    @Override
    @DS("#last")
    public Boolean syncDropTable(String tableName, List<String> appTableColumns, String dsName) {
        Map<String, Object> sqlParamMap = new HashMap<>();
        sqlParamMap.put("tableName", tableName);
        sqlParamMap.put("columns", appTableColumns);

        return appMapper.createTable(writer(sqlParamMap, "drop-table.vm")) >= 0;
    }

    @SneakyThrows
    private static String writer(Map<String, Object> sqlParamMap, String template) {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template tpl = engine.getTemplate(template);
        try (StringWriter result = new StringWriter()) {
            tpl.render(sqlParamMap, result);
            return result.toString();
        }
    }

}
