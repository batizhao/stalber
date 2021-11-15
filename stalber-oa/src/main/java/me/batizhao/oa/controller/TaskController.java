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
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

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
    @ApiOperation(value = "获取流程定义")
    @GetMapping(value = "/process", params = "key")
    public R<InitProcessDefView> handleProcessDefinition(@ApiParam(value = "key" , required = true) @RequestParam("key") @Size(min = 1) String key) {
        return R.ok(taskService.findProcessDefinitionByKey(key));
    }

    /**
     * 获取环节的输出路由及路由后的任务环节配置信息
     * @param processDefinitionId 流程定义Id
     * @param taskDefKey 流程环节key
     * @return R
     * @real_return R<List<ProcessRouterView>>
     */
    @ApiOperation(value = "获取环节的输出路由及路由后的任务环节配置信息")
    @GetMapping(value = "/process/{processDefinitionId}/{taskDefKey}")
    public R<List<ProcessRouterView>> handleProcessRouter(@ApiParam(value = "processDefinitionId" , required = true) @PathVariable("processDefinitionId") @Size(min = 1) String processDefinitionId,
                                                          @ApiParam(value = "taskDefKey" , required = true) @PathVariable("taskDefKey") @Size(min = 1) String taskDefKey) {
        return R.ok(taskService.findProcessRouter(processDefinitionId, taskDefKey));
    }

    /**
     * 待办任务
     * @param appTodoTaskDTO
     * @return R
     * @real_return R<Page<TodoTaskView>>
     */
    @ApiOperation(value = "待办任务")
    @GetMapping("/task/todo")
    public R<IPage<TodoTaskView>> handleTodoTasks(Page page, AppTodoTaskDTO appTodoTaskDTO) {
        return R.ok(taskService.findTodoTasks(page, appTodoTaskDTO));
    }

    /**
     * 已办任务
     * @param appTodoTaskDTO
     * @return
     */
    @ApiOperation(value = "已办任务")
    @GetMapping("/task/done")
    public R<IPage<TodoTaskView>> handleDoneTasks(Page page, AppTodoTaskDTO appTodoTaskDTO) {
        return R.ok(taskService.findDoneTasks(page, appTodoTaskDTO));
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

    /**
     * 获取流程指定环节意见
     *
     * @param procInstId 流程实例Id
     * @param taskDefKeyList 指定环节
     * @param orderRule 排序规则 0 时间升序排， 1 先按人员职位排序，同级别时间升序排
     * @return R<List<ProcessMessageView>>
     */
    @ApiOperation(value = "获取流程指定环节意见")
    @GetMapping("/comments")
    public R<List<ProcessMessageView>> handleComment(@ApiParam(value = "procInstId", required = true) @RequestParam("procInstId") @Size(min = 1) String procInstId,
                                                     @ApiParam(value = "taskDefKeyList", required = true) @RequestParam("taskDefKeyList") List<String> taskDefKeyList,
                                                     @ApiParam(value = "orderRule", required = true) @RequestParam("orderRule") Integer orderRule) {
        return R.ok(taskService.loadMessage(procInstId, taskDefKeyList, orderRule));
    }

    /**
     * 签收
     * @param taskId 任务Id
     * @param type 任务类型：0 审批任务、 1 传阅任务
     * @return
     */
    @ApiOperation(value = "签收")
    @PostMapping("/task/sign")
    public R<Boolean> handleSign(@ApiParam(value = "taskId", required = true) @RequestParam("taskId") String taskId,
                                 @ApiParam(value = "type", defaultValue = "0") @RequestParam("type") String type) {
        return R.ok(taskService.sign(taskId, type));
    }

}
