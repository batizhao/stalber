package me.batizhao.app.service.impl;

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
import lombok.SneakyThrows;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableColumn;
import me.batizhao.app.mapper.AppTableMapper;
import me.batizhao.app.service.AppTableService;
import me.batizhao.common.core.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public IPage<AppTable> findAppTables(Page<AppTable> page, AppTable appTable) {
        LambdaQueryWrapper<AppTable> wrapper = Wrappers.lambdaQuery();
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
//        Map<String, Object> sqlParamMap = new HashMap<>();
//        sqlParamMap.put("tableName", appTable.getTableName());
//        sqlParamMap.put("tableComment", appTable.getTableComment());
//
//        JSONArray array = JSONUtil.parseArray(appTable.getColumnMetadata());
//        List<AppTableColumn> appTableColumns = JSONUtil.toList(array, AppTableColumn.class);
//        sqlParamMap.put("columns", appTableColumns);
//
//        writer(sqlParamMap);

        if (appTable.getId() == null) {
            appTable.setCreateTime(LocalDateTime.now());
            appTable.setUpdateTime(LocalDateTime.now());
            appTableMapper.insert(appTable);
        } else {
            appTable.setUpdateTime(LocalDateTime.now());
            appTableMapper.updateById(appTable);
        }
        return appTable;
    }

    @SneakyThrows
    private static String writer(Map<String, Object> sqlParamMap) {
        TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig("templates", TemplateConfig.ResourceMode.CLASSPATH));
        Template tpl = engine.getTemplate("table.vm");
        try (StringWriter result = new StringWriter()) {
            tpl.render(sqlParamMap, result);
            return result.toString();
        }
    }

}
