package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.dto.ApplicationDTO;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.ProcessRouterView;
import me.batizhao.terrace.vo.TaskNodeView;
import me.batizhao.terrace.vo.TodoTaskView;

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
     * 分页查询
     * @param page 分页对象
     * @param todoTaskView 任务对象
     * @return IPage<TodoTaskView>
     */
    IPage<TodoTaskView> findTasks(Page<TodoTaskView> page, TodoTaskView todoTaskView);

    /**
     * 通过id查询
     * @param id id
     * @return Comment
     */
    TaskNodeView findById(Long id);

    /**
     * 启动流程
     * @param appId 业务Id
     * @param appTitle 业务标题
     * @return
     */
    String start(String appId, String appTitle);

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
}
