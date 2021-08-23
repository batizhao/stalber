package me.batizhao.dp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.dp.domain.FormHistory;
import me.batizhao.dp.mapper.FormHistoryMapper;
import me.batizhao.dp.service.FormHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 单历史记录接口实现类
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Service
public class FormHistoryServiceImpl extends ServiceImpl<FormHistoryMapper, FormHistory> implements FormHistoryService {

    @Autowired
    private FormHistoryMapper formHistoryMapper;

    @Override
    public List<FormHistory> findByFormKey(String formKey) {
        LambdaQueryWrapper<FormHistory> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(formKey)) {
            wrapper.eq(FormHistory::getFormKey, formKey);
        }
        return formHistoryMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public FormHistory saveFormHistory(String formKey, String metadata) {
        FormHistory formHistory = new FormHistory().setFormKey(formKey).setMetadata(metadata).setCreateTime(LocalDateTime.now());
        formHistoryMapper.insert(formHistory);
        return formHistory;
    }


}