package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.TaskNodeView;
import me.batizhao.terrace.vo.TodoTaskView;

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
     * 提交任务
     * @param task 任务
     * @return
     */
    String submit(Task task);
}
