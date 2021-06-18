package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
}
