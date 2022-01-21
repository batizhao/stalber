package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.App;

import java.util.List;

/**
 * 应用接口类
 *
 * @author batizhao
 * @since 2022-01-21
 */
public interface AppService extends IService<App> {

    /**
     * 分页查询应用
     * @param page 分页对象
     * @param app 应用
     * @return IPage<App>
     */
    IPage<App> findApps(Page<App> page, App app);

    /**
     * 查询应用
     * @param app
     * @return List<App>
     */
    List<App> findApps(App app);

    /**
     * 通过id查询应用
     * @param id id
     * @return App
     */
    App findById(Long id);

    /**
     * 添加或编辑应用
     * @param app 应用
     * @return App
     */
    App saveOrUpdateApp(App app);

    /**
     * 更新应用状态
     * @param app 应用
     * @return Boolean
     */
    Boolean updateStatus(App app);

}
