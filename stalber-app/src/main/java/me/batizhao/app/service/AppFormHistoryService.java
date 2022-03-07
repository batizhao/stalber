package me.batizhao.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppFormHistory;

import java.util.List;

/**
 * 单历史记录接口类
 *
 * @author batizhao
 * @since 2021-08-12
 */
public interface AppFormHistoryService extends IService<AppFormHistory> {

    /**
     * 通过formKey查询历史记录
     * @param formKey
     * @return List<FormHistory>
     */
    List<AppFormHistory> findByFormKey(String formKey);

    /**
     * 添加表单历史记录
     * @param formKey
     * @param metadata
     * @return
     */
    AppFormHistory saveFormHistory(String formKey, String metadata);

}