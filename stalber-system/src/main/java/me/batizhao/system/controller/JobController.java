package me.batizhao.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.exception.TaskException;
import me.batizhao.common.util.R;
import me.batizhao.system.domain.SysJob;
import me.batizhao.system.service.JobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 任务调度 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-05-07
 */
@Api(tags = "任务调度管理")
@RestController
@Slf4j
@Validated
@RequestMapping("system")
public class JobController {

    @Autowired
    private JobService jobService;

    /**
     * 分页查询任务调度
     * @param page 分页对象
     * @param job 任务调度
     * @return R
     * @real_return R<Page<Job>>
     */
    @ApiOperation(value = "分页查询任务调度")
    @GetMapping("/jobs")
    @PreAuthorize("@pms.hasPermission('system:job:admin')")
    public R<IPage<SysJob>> handleJobs(Page<SysJob> page, SysJob job) {
        return R.ok(jobService.findJobs(page, job));
    }

    /**
     * 查询任务调度
     * @return R<List<Job>>
     */
    @ApiOperation(value = "查询任务调度")
    @GetMapping("/job")
    @PreAuthorize("@pms.hasPermission('system:job:admin')")
    public R<List<SysJob>> handleJobs(SysJob job) {
        return R.ok(jobService.findJobs(job));
    }

    /**
     * 通过id查询任务调度
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询任务调度")
    @GetMapping("/job/{id}")
    @PreAuthorize("@pms.hasPermission('system:job:admin')")
    public R<SysJob> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(jobService.findById(id));
    }

    /**
     * 添加或编辑任务调度
     * @param job 任务调度
     * @return R
     */
    @ApiOperation(value = "添加或编辑任务调度")
    @PostMapping("/job")
    @PreAuthorize("@pms.hasPermission('system:job:add') or @pms.hasPermission('system:job:edit')")
    public R<SysJob> handleSaveOrUpdate(@Valid @ApiParam(value = "任务调度" , required = true) @RequestBody SysJob job) throws SchedulerException, TaskException {
        return R.ok(jobService.saveOrUpdateJob(job));
    }

    /**
     * 通过id删除任务调度
     * @param ids ID串
     * @return R
     */
    @ApiOperation(value = "通过id删除任务调度")
    @DeleteMapping("/job")
    @PreAuthorize("@pms.hasPermission('system:job:delete')")
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(jobService.removeByIds(ids));
    }

    /**
     * 更新任务调度状态
     *
     * @param job 任务调度
     * @return R
     */
    @ApiOperation(value = "更新任务调度状态")
    @PostMapping("/job/status")
    @PreAuthorize("@pms.hasPermission('system:job:admin')")
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "任务调度" , required = true) @RequestBody SysJob job) throws SchedulerException {
        return R.ok(jobService.updateStatus(job));
    }

    /**
     * 立即执行任务
     *
     * @param job 任务调度
     * @return R
     */
    @ApiOperation(value = "立即执行任务")
    @PostMapping("/job/run")
    @PreAuthorize("@pms.hasPermission('system:job:admin')")
    public R<Boolean> handleJobRun(@ApiParam(value = "任务调度" , required = true) @RequestBody SysJob job) throws SchedulerException {
        return R.ok(jobService.run(job));
    }

}
