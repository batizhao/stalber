package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.PageModel;

import java.util.List;

/**
 * <p> 页面模型接口类 </p>
 *
 * @author wws
 * @since 2022-02-18 13:04
 */
public interface PageModelService extends IService<PageModel> {

    /**
     * 分页查询页面模型表
     * @param page 分页对象
     * @param pageModel 页面模型表对象
     * @return IPage<PageModel>
     */
    IPage<PageModel> findPageModelTables(Page<PageModel> page, PageModel pageModel);

    /**
     * 查询应用表
     * @param pageModel 页面模型表对象
     * @return List<PageModel>
     */
    List<PageModel> findPageModelTable(PageModel pageModel);

    /**
     * 添加或编辑页面模型表
     * @param pageModel 页面模型表对象
     * @return PageModel
     */
    PageModel saveOrUpdatePageModelTable(PageModel pageModel);
}
