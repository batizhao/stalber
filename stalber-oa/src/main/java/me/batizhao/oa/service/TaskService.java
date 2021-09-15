package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import feign.Param;
import me.batizhao.oa.domain.Invoice;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.dto.ApplicationDTO;
import me.batizhao.terrace.vo.*;

import java.util.List;

/**
 * 审批接口类
 *
 * @author batizhao
 * @since 2021-06-10
 */
public interface TaskService {

    /**
     * 获取流程定义
     * @param key
     * @return
     */
    InitProcessDefView findProcessDefinitionByKey(String key);

    /**
     * 待办任务
     * @param todoTaskView 任务对象
     * @return IPage<TodoTaskView>
     */
    IPage<TodoTaskView> findTodoTasks(Page page, TodoTaskView todoTaskView);

    /**
     * 已办任务
     * @param appTodoTaskDTO
     * @return
     */
    IPage<TodoTaskView> findDoneTasks(Page page, AppTodoTaskDTO appTodoTaskDTO);

    /**
     * 通过id查询
     * @param id id
     * @return Comment
     */
    TaskNodeView findById(Long id);

    /**
     * 启动流程
     * @param task 任务
     * @return
     */
    String start(Task task);

    /**
     * 提交任务
     * @param task 任务
     * @return
     */
    String submit(Task task);

    /**
     * 获取环节的输出路由及路由后的任务环节配置信息
     *
     * @param processDefinitionId 流程定义Id
     * @param taskDefKey 流程环节key
     * @return
     */
    List<ProcessRouterView> findProcessRouter(String processDefinitionId, String taskDefKey);

    /**
     * 获取流程指定环节意见
     *
     * @param procInstId 流程实例Id
     * @param taskDefKeyList 指定环节
     * @param orderRule 排序规则 0 时间升序排， 1 先按人员职位排序，同级别时间升序排
     * @return
     */
    List<ProcessMessageView> loadMessage(String procInstId, List<String> taskDefKeyList, Integer orderRule);
}
