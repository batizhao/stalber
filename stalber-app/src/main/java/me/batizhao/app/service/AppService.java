package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.App;
import me.batizhao.app.domain.AppTable;

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

    /**
     * 同步表到数据库
     * 这个动态数据源方法要独立封装，不能和调用者放到同一个类中
     * @param appTable 应用表元数据
     * @return
     */
    Boolean syncTableToDB(AppTable appTable, String dsName);

}
