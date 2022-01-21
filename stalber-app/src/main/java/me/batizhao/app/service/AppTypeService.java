package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppType;

import java.util.List;

/**
 * 应用分类接口类
 *
 * @author batizhao
 * @since 2022-01-21
 */
public interface AppTypeService extends IService<AppType> {

    /**
     * 分页查询应用分类
     * @param page 分页对象
     * @param appType 应用分类
     * @return IPage<AppType>
     */
    IPage<AppType> findAppTypes(Page<AppType> page, AppType appType);

    /**
     * 查询应用分类
     * @param appType
     * @return List<AppType>
     */
    List<AppType> findAppTypes(AppType appType);

    /**
     * 通过id查询应用分类
     * @param id id
     * @return AppType
     */
    AppType findById(Long id);

    /**
     * 添加或编辑应用分类
     * @param appType 应用分类
     * @return AppType
     */
    AppType saveOrUpdateAppType(AppType appType);

    /**
     * 更新应用分类状态
     * @param appType 应用分类
     * @return Boolean
     */
    Boolean updateStatus(AppType appType);

}
