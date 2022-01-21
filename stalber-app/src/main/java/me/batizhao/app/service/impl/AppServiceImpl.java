package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.app.domain.App;
import me.batizhao.app.mapper.AppMapper;
import me.batizhao.app.service.AppService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

}
