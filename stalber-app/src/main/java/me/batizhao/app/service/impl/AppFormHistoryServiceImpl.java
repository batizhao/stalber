package me.batizhao.app.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.app.domain.AppFormHistory;
import me.batizhao.app.mapper.AppFormHistoryMapper;
import me.batizhao.app.service.AppFormHistoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

/**
 * 单历史记录接口实现类
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Service
public class AppFormHistoryServiceImpl extends ServiceImpl<AppFormHistoryMapper, AppFormHistory> implements AppFormHistoryService {

    @Autowired
    private AppFormHistoryMapper appFormHistoryMapper;

    @Override
    public List<AppFormHistory> findByFormKey(String formKey) {
        LambdaQueryWrapper<AppFormHistory> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(formKey)) {
            wrapper.eq(AppFormHistory::getFormKey, formKey);
        }
        return appFormHistoryMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public AppFormHistory saveFormHistory(String formKey, String metadata) {
        int version = 1;
        if (!this.findByFormKey(formKey).isEmpty()) {
            version = this.findByFormKey(formKey).stream().max(Comparator.comparing(AppFormHistory::getVersion)).get().getVersion() + 1 ;
        }
        AppFormHistory AppFormHistory = new AppFormHistory()
                .setVersion(version)
                .setFormKey(formKey)
                .setMetadata(metadata)
                .setCreateTime(LocalDateTime.now());
        appFormHistoryMapper.insert(AppFormHistory);
        return AppFormHistory;
    }

}