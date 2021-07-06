package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.oa.domain.Task;
import me.batizhao.oa.service.TaskService;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.dto.AppTodoTaskDTO;
import me.batizhao.terrace.dto.ApplicationDTO;
import me.batizhao.terrace.dto.StartProcessDTO;
import me.batizhao.terrace.dto.SubmitProcessDTO;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.ProcessRouterView;
import me.batizhao.terrace.vo.TaskNodeView;
import me.batizhao.terrace.vo.TodoTaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批接口实现类
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TerraceApi terraceApi;

    @Override
    public InitProcessDefView findProcessDefinitionByKey(String key) {
        return terraceApi.loadProcessDefinitionByKey(key).getData();
    }

    @Override
    public IPage<TodoTaskView> findTasks(TodoTaskView todoTaskView) {
        AppTodoTaskDTO dto = new AppTodoTaskDTO();
        dto.setBusinessModuleId("12");
        dto.setUserName("1");
        dto.setQueryType("1");

        return terraceApi.loadTasks(dto).getData();
    }

    @Override
    public TaskNodeView findById(Long id) {
        return terraceApi.loadTaskDetail(id.toString(), "0").getData();
    }

    @Override
    public String start(Task task) {
        StartProcessDTO dto = new StartProcessDTO();
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setCurrent(task.getCurrent());
        dto.setUserId("1");
        dto.setUserName("admin");
        dto.setTenantId("23");
        dto.setOrgId("1");
        dto.setOrgName("jiangsu");
        dto.setDraft(false);
        dto.setProcessNodeDTO(task.getProcessNodeDTO());

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setId(task.getId());
        applicationDTO.setCode("xxx");
        applicationDTO.setModuleId("12");
        applicationDTO.setModuleName("oa");
        applicationDTO.setTitle(task.getTitle());
        applicationDTO.setCreator("admin");
        dto.setDto(applicationDTO);

        log.info("StartProcessDTO : {}", dto);

        return terraceApi.start(dto).getData();
    }

    @Override
    public String submit(Task task) {
        SubmitProcessDTO dto = new SubmitProcessDTO();
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setCurrent(task.getCurrent());
        dto.setUserId("1");
        dto.setUserName("admin");
        dto.setTenantId("23");
        dto.setOrgId("1");
        dto.setOrgName("jiangsu");
        dto.setTaskId(task.getTaskId());
        dto.setProcInstId(task.getProcInstId());
        dto.setProcessNodeDTO(task.getProcessNodeDTO());

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setId(task.getId());
        applicationDTO.setCode("xxx");
        applicationDTO.setModuleId("12");
        applicationDTO.setModuleName("oa");
        applicationDTO.setTitle(task.getTitle());
        applicationDTO.setCreator("admin");
        dto.setDto(applicationDTO);

        return terraceApi.submit(dto).getData();
    }

    @Override
    public List<ProcessRouterView> findProcessRouter(String processDefinitionId, String taskDefKey) {
        return terraceApi.loadProcessRouter(taskDefKey, processDefinitionId).getData();
    }
}
