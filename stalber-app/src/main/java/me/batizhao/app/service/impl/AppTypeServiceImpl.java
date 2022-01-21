package me.batizhao.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.app.domain.AppType;
import me.batizhao.app.mapper.AppTypeMapper;
import me.batizhao.app.service.AppTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用分类接口实现类
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Service
public class AppTypeServiceImpl extends ServiceImpl<AppTypeMapper, AppType> implements AppTypeService {

    @Autowired
    private AppTypeMapper appTypeMapper;

    @Override
    public IPage<AppType> findAppTypes(Page<AppType> page, AppType appType) {
        LambdaQueryWrapper<AppType> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(appType.getName())) {
            wrapper.like(AppType::getName, appType.getName());
        }
        return appTypeMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AppType> findAppTypes(AppType appType) {
        LambdaQueryWrapper<AppType> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(appType.getName())) {
            wrapper.like(AppType::getName, appType.getName());
        }
        return appTypeMapper.selectList(wrapper);
    }

    @Override
    public AppType findById(Long id) {
        AppType appType = appTypeMapper.selectById(id);

        if(appType == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return appType;
    }

    @Override
    @Transactional
    public AppType saveOrUpdateAppType(AppType appType) {
        if (appType.getId() == null) {
            appType.setCreateTime(LocalDateTime.now());
            appType.setUpdateTime(LocalDateTime.now());
            appTypeMapper.insert(appType);
        } else {
            appType.setUpdateTime(LocalDateTime.now());
            appTypeMapper.updateById(appType);
        }

        return appType;
    }

    @Override
    @Transactional
    public Boolean updateStatus(AppType appType) {
        LambdaUpdateWrapper<AppType> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AppType::getId, appType.getId()).set(AppType::getStatus, appType.getStatus());
        return appTypeMapper.update(null, wrapper) == 1;
    }

}
