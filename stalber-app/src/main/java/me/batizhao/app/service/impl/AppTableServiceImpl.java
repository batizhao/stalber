package me.batizhao.app.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableColumn;
import me.batizhao.app.mapper.AppTableMapper;
import me.batizhao.app.service.AppService;
import me.batizhao.app.service.AppTableService;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.StalberException;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.service.CodeMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

}
