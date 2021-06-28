package me.batizhao.oa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.oa.domain.Task;
import me.batizhao.oa.service.TaskService;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.TaskNodeView;
import me.batizhao.terrace.vo.TodoTaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * 流程任务管理 API
 *
 * @module oa
 *
 * @author batizhao
 * @since 2021-06-18
 */
@Api(tags = "任务管理")
@RestController
@Slf4j
@Validated
@RequestMapping("oa")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 获取流程定义
     * @param key 流程key
     * @return R
     * @real_return R<InitProcessDefView>
     */
    @ApiOperation(value = "分页查询")
    @GetMapping(value = "/task", params = "key")
    public R<InitProcessDefView> handleProcessDefinition(@ApiParam(value = "key" , required = true) @RequestParam("key") @Size(min = 1) String key) {
        return R.ok(taskService.findProcessDefinitionByKey(key));
    }

    /**
     * 分页查询
     * @param page 分页对象
     * @param taskView 任务对象
     * @return R
     * @real_return R<Page<TodoTaskView>>
     */
    @ApiOperation(value = "分页查询")
    @GetMapping("/tasks")
    public R<IPage<TodoTaskView>> handleTasks(Page<TodoTaskView> page, TodoTaskView taskView) {
        return R.ok(taskService.findTasks(page, taskView));
    }

    /**
     * 通过id查询
     * @param id id
     * @return R<TaskNodeView>
     */
    @ApiOperation(value = "通过id查询")
    @GetMapping(value = "/task", params = "id")
    public R<TaskNodeView> handleId(@ApiParam(value = "ID" , required = true) @RequestParam("id") @Min(1) Long id) {
        return R.ok(taskService.findById(id));
    }

    /**
     * 提交任务
     * @param task 任务
     * @return R<String>
     */
    @ApiOperation(value = "提交任务")
    @PostMapping("/task")
    public R<String> handleSubmit(@Valid @ApiParam(value = "任务" , required = true) @RequestBody Task task) {
        return R.ok(taskService.submit(task));
    }

}