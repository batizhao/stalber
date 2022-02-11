package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.mapper.AppTableMapper;
import me.batizhao.app.service.AppService;
import me.batizhao.app.service.AppTableService;
import me.batizhao.common.core.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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
        appService.syncTableToDB(appTable, appTable.getDsName());

        appTable.setUpdateTime(LocalDateTime.now());
        appTable.setStatus("synced");
        appTableMapper.updateById(appTable);
        return true;
    }

}
