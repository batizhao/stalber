package me.batizhao.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.app.domain.AppProcess;

import java.util.List;

/**
 * <p> 应用流程接口清单 </p>
 *
 * @author wws
 * @since 2022-02-28 15:03
 */
public interface AppProcessService extends IService<AppProcess> {

    /**
     * 分页查询应用流程表
     * @param page 分页对象
     * @param appProcess 应用流程表
     * @return IPage<AppProcess>
     */
    IPage<AppProcess> findAppProcess(Page<AppProcess> page, AppProcess appProcess);

    /**
     * 查询应用流程表
     * @param appProcess 应用流程表
     * @return List<AppTable>
     */
    List<AppProcess> findAppProcess(AppProcess appProcess);

    /**
     * 查询应用流程表
     * @param appId 应用Id
     * @param version 版本号
     * @return AppTable
     */
    AppProcess findAppProcess(Long appId, Integer version);

    /**
     * 通过id查询应用流程表
     * @param id id
     * @return AppProcess
     */
    AppProcess findById(Long id);

    /**
     * 添加或编辑应用流程表
     * @param appProcess 应用流程表
     * @return AppProcess
     */
    AppProcess saveOrUpdateAppProcess(AppProcess appProcess);

    /**
     * 更新应用流程状态
     * @param appProcess 应用流程
     * @return Boolean
     */
    Boolean updateStatus(AppProcess appProcess);
}
