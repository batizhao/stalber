package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.oa.domain.Comment;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.vo.TodoTaskView;

/**
 * 审批接口类
 *
 * @author batizhao
 * @since 2021-06-10
 */
public interface TaskService {

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
    Object findById(Long id);

    /**
     * 提交任务
     * @param task 任务
     * @return
     */
    Object submit(Task task);
}
