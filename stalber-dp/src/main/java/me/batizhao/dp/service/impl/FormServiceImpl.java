package me.batizhao.dp.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.dp.domain.Form;
import me.batizhao.dp.mapper.FormMapper;
import me.batizhao.dp.service.FormHistoryService;
import me.batizhao.dp.service.FormService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 表单接口实现类
 *
 * @author batizhao
 * @since 2021-03-08
 */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements FormService {

    @Autowired
    private FormMapper formMapper;
    @Autowired
    private FormHistoryService formHistoryService;

    @Override
    public IPage<Form> findForms(Page<Form> page, Form form) {
        LambdaQueryWrapper<Form> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(form.getName())) {
            wrapper.like(Form::getName, form.getName());
        }
        return formMapper.selectPage(page, wrapper);
    }

    @Override
    public Form findById(Long id) {
        Form form = formMapper.selectById(id);

        if(form == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return form;
    }

    @Override
    @Transactional
    public Form saveOrUpdateForm(Form form) {
        if (form.getId() == null) {
            form.setCreateTime(LocalDateTime.now());
            form.setUpdateTime(LocalDateTime.now());
            form.setFormKey(IdUtil.objectId());
            formMapper.insert(form);
        } else {
            form.setUpdateTime(LocalDateTime.now());
            formMapper.updateById(form);
        }

        formHistoryService.saveFormHistory(form.getFormKey(), form.getMetadata());
        return form;
    }

    @Override
    @Transactional
    public Boolean updateStatus(Form form) {
        LambdaUpdateWrapper<Form> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(Form::getId, form.getId()).set(Form::getStatus, form.getStatus());
        return formMapper.update(null, wrapper) == 1;
    }

}
