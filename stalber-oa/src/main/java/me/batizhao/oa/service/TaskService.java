package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.dto.StartProcessDTO;
import me.batizhao.terrace.dto.SubmitProcessDTO;
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
     * @param appTodoTaskDTO
     * @return IPage<TodoTaskView>
     */
    IPage<TodoTaskView> findTodoTasks(Page page, AppTodoTaskDTO appTodoTaskDTO);

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
     * @param dto 任务
     * @return
     */
    String start(StartProcessDTO dto);

    /**
     * 提交任务
     * @param dto 任务
     * @return
     */
    String submit(SubmitProcessDTO dto);

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

    /**
     * 签收
     * @param taskId
     * @param type
     * @return
     */
    Boolean sign(String taskId, String type);

    /**
     * 候选人
     * @param processInstId 流程实例Id
     * @param taskDefKey 流程定义Id
     * @param taskId 任务Id
     * @param back 是否退回
     * @param processDefId 流程定义Id
     * @param orgId 任务组织Id
     * @return
     */
    List<QueryCandidateView> loadCandidate(String processInstId, String taskDefKey, String taskId, Boolean back, String processDefId, String orgId);
}
