package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppList;

import java.util.List;

/**
 * <p> 应用列表接口 </p>
 *
 * @author wws
 * @since 2022-03-02 19:58
 */
public interface AppListService extends IService<AppList> {

    /**
     * 分页查询应用列表
     * @param page 分页对象
     * @param appList 应用列表
     * @return IPage<AppList>
     */
    IPage<AppList> findAppList(Page<AppList> page, AppList appList);

    /**
     * 查询应用列表
     * @param appList 应用列表
     * @return List<AppTable>
     */
    List<AppList> findAppList(AppList appList);

    /**
     * 通过id查询应用列表
     * @param id id
     * @return AppList
     */
    AppList findById(Long id);

    /**
     * 添加或编辑应用列表
     * @param appList 应用列表
     * @return AppList
     */
    AppList saveOrUpdateAppList(AppList appList);

    /**
     * 更新应用列表状态
     * @param appList 应用列表
     * @return Boolean
     */
    Boolean updateStatus(AppList appList);

    /**
     * 通过编号查询应用列表
     * @param code 列表编号
     * @return R
     */
    AppList findByCode(String code);
}
