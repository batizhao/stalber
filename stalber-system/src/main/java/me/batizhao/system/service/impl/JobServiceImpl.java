package me.batizhao.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.constant.ScheduleConstants;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.common.exception.TaskException;
import me.batizhao.system.domain.SysJob;
import me.batizhao.system.mapper.JobMapper;
import me.batizhao.system.service.JobService;
import me.batizhao.system.util.CronUtils;
import me.batizhao.system.util.ScheduleUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务调度接口实现类
 *
 * @author batizhao
 * @since 2021-05-07
 */
@Service
public class JobServiceImpl extends ServiceImpl<JobMapper, SysJob> implements JobService {

    @Autowired
    private JobMapper jobMapper;
    @Autowired
    private Scheduler scheduler;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<SysJob> jobList = list();
        for (SysJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public IPage<SysJob> findJobs(Page<SysJob> page, SysJob job) {
        LambdaQueryWrapper<SysJob> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(job.getName())) {
            wrapper.like(SysJob::getName, job.getName());
        }
        return jobMapper.selectPage(page, wrapper);
    }

    @Override
    public List<SysJob> findJobs(SysJob job) {
        LambdaQueryWrapper<SysJob> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(job.getName())) {
            wrapper.like(SysJob::getName, job.getName());
        }
        return jobMapper.selectList(wrapper);
    }

    @Override
    public SysJob findById(Long id) {
        SysJob job = jobMapper.selectById(id);

        if (job == null) {
            throw new NotFoundException(String.format("没有该记录 '%s'。", id));
        }

        return job;
    }

    @Override
    @Transactional
    public SysJob saveOrUpdateJob(SysJob job) throws SchedulerException, TaskException {
        if (!CronUtils.isValid(job.getCronExpression()))
            throw new TaskException("表达式语法不正确！", TaskException.Code.CRON_INVALID);

        if (job.getId() == null) {
            job.setCreateTime(LocalDateTime.now());
            job.setUpdateTime(LocalDateTime.now());
            int row = jobMapper.insert(job);
            if (row > 0) {
                ScheduleUtils.createScheduleJob(scheduler, job);
            }
        } else {
            //Old Group
            SysJob properties = findById(job.getId());
            job.setUpdateTime(LocalDateTime.now());
            int row = jobMapper.updateById(job);
            if (row > 0) {
                updateSchedulerJob(job, properties.getJobGroup());
            }
        }

        return job;
    }

    @Override
    @Transactional
    public Boolean updateStatus(SysJob job) throws SchedulerException {
        String status = job.getStatus();
        if (ScheduleConstants.Status.OPEN.getValue().equals(status)) {
            resumeJob(job);
        } else if (ScheduleConstants.Status.CLOSE.getValue().equals(status)) {
            pauseJob(job);
        }

        LambdaUpdateWrapper<SysJob> wrapper = Wrappers.lambdaUpdate();
        wrapper.eq(SysJob::getId, job.getId()).set(SysJob::getStatus, status);
        return jobMapper.update(null, wrapper) == 1;
    }

    @Override
    @Transactional
    public Boolean run(SysJob job) throws SchedulerException {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        SysJob properties = findById(jobId);

        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
        return true;
    }

    /**
     * 更新任务
     *
     * @param job      任务对象
     * @param jobGroup 任务组名
     */
    private void updateSchedulerJob(SysJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 暂停任务
     *
     * @param job 调度信息
     */
    private int pauseJob(SysJob job) throws SchedulerException
    {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.CLOSE.getValue());
        int row = jobMapper.updateById(job);
        if (row > 0)
        {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return row;
    }

    /**
     * 恢复任务
     *
     * @param job 调度信息
     */
    private int resumeJob(SysJob job) throws SchedulerException
    {
        Long jobId = job.getId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.OPEN.getValue());
        int row = jobMapper.updateById(job);
        if (row > 0)
        {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return row;
    }
}
