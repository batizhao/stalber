package me.batizhao.system.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.common.core.exception.TaskException;
import me.batizhao.system.domain.SysJob;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * 任务调度接口类
 *
 * @author batizhao
 * @since 2021-05-07
 */
public interface JobService extends IService<SysJob> {

    /**
     * 分页查询任务调度
     * @param page 分页对象
     * @param job 任务调度
     * @return IPage<Job>
     */
    IPage<SysJob> findJobs(Page<SysJob> page, SysJob job);

    /**
     * 查询任务调度
     * @param job
     * @return List<Job>
     */
    List<SysJob> findJobs(SysJob job);

    /**
     * 通过id查询任务调度
     * @param id id
     * @return Job
     */
    SysJob findById(Long id);

    /**
     * 添加或编辑任务调度
     * @param job 任务调度
     * @return Job
     */
    SysJob saveOrUpdateJob(SysJob job) throws SchedulerException, TaskException;

    /**
     * 更新任务调度状态
     * @param job 任务调度
     * @return Boolean
     */
    Boolean updateStatus(SysJob job) throws SchedulerException;

    /**
     * 立即执行任务
     * @param job 任务调度
     * @return Boolean
     */
    Boolean run(SysJob job) throws SchedulerException;
}
