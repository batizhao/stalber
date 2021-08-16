package me.batizhao.dp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.dp.domain.FormHistory;

import java.util.List;

/**
 * 单历史记录接口类
 *
 * @author batizhao
 * @since 2021-08-12
 */
public interface FormHistoryService extends IService<FormHistory> {

    /**
     * 通过formKey查询历史记录
     * @param formKey
     * @return List<FormHistory>
     */
    List<FormHistory> findByFormKey(String formKey);

    /**
     * 添加表单历史记录
     * @param formKey
     * @param metadata
     * @return
     */
    FormHistory saveFormHistory(String formKey, String metadata);

}