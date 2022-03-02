package me.batizhao.app.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.app.domain.AppForm;
import me.batizhao.app.mapper.AppFormMapper;
import me.batizhao.app.service.AppFormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用表单接口实现类
 *
 * @author batizhao
 * @since 2022-02-24
 */
@Service
public class AppFormServiceImpl extends ServiceImpl<AppFormMapper, AppForm> implements AppFormService {

    @Autowired
    private AppFormMapper appFormMapper;

    @Override
    public IPage<AppForm> findAppForms(Page<AppForm> page, AppForm appForm) {
        LambdaQueryWrapper<AppForm> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(appForm.getName())) {
            wrapper.like(AppForm::getName, appForm.getName());
        }
        return appFormMapper.selectPage(page, wrapper);
    }

    @Override
    public List<AppForm> findAppForms(AppForm appForm) {
        LambdaQueryWrapper<AppForm> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(appForm.getName())) {
            wrapper.like(AppForm::getName, appForm.getName());
        }
        return appFormMapper.selectList(wrapper);
    }

    @Override
    public AppForm findById(Long id) {
        AppForm appForm = appFormMapper.selectById(id);

        if(appForm == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return appForm;
    }

    @Override
    @Transactional
    public AppForm saveOrUpdateAppForm(AppForm appForm) {
        if (appForm.getId() == null) {
            appForm.setCreateTime(LocalDateTime.now());
            appForm.setUpdateTime(LocalDateTime.now());
            appForm.setFormKey(IdUtil.objectId());
            appFormMapper.insert(appForm);
        } else {
            appForm.setUpdateTime(LocalDateTime.now());
            appFormMapper.updateById(appForm);
        }

        return appForm;
    }


    @Override
    @Transactional
    public Boolean updateStatus(AppForm appForm) {
        LambdaUpdateWrapper<AppForm> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(AppForm::getId, appForm.getId()).set(AppForm::getStatus, appForm.getStatus());
        return appFormMapper.update(null, wrapper) == 1;
    }

}
