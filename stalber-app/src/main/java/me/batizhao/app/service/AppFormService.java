package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppForm;

import java.util.List;

/**
 * 应用表单接口类
 *
 * @author batizhao
 * @since 2022-02-24
 */
public interface AppFormService extends IService<AppForm> {

    /**
     * 分页查询应用表单
     * @param page 分页对象
     * @param appForm 应用表单
     * @return IPage<AppForm>
     */
    IPage<AppForm> findAppForms(Page<AppForm> page, AppForm appForm);

    /**
     * 查询应用表单
     * @param appForm
     * @return List<AppForm>
     */
    List<AppForm> findAppForms(AppForm appForm);

    /**
     * 通过id查询应用表单
     * @param id id
     * @return AppForm
     */
    AppForm findById(Long id);

    /**
     * 添加或编辑应用表单
     * @param appForm 应用表单
     * @return AppForm
     */
    AppForm saveOrUpdateAppForm(AppForm appForm);

    /**
     * 更新应用表单状态
     * @param appForm 应用表单
     * @return Boolean
     */
    Boolean updateStatus(AppForm appForm);

    /**
     * 通过id恢复表单历史记录
     * @param id
     * @return
     */
    Boolean revertFormById(Long id);

}
