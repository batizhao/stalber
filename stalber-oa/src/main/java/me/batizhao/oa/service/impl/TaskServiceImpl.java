package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.oa.service.TaskService;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.vo.TodoTaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 审批接口实现类
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TerraceApi terraceApi;

    @Override
    public IPage<TodoTaskView> findTasks(Page<TodoTaskView> page, TodoTaskView todoTaskView) {
        AppTodoTaskDTO dto = new AppTodoTaskDTO();
        dto.setBusinessModuleId("12");
        dto.setUserName("1");

        return terraceApi.loadTasks(dto).getData();
    }

}
