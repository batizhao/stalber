package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.SecurityUtils;
import me.batizhao.oa.domain.Task;
import me.batizhao.oa.service.TaskService;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.dto.*;
import me.batizhao.terrace.vo.*;
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
    public IPage<TodoTaskView> findTodoTasks(Page page, AppTodoTaskDTO appTodoTaskDTO) {
        return terraceApi.loadTodoTasks(page.getCurrent(), page.getSize(),
                SecurityUtils.getUser().getUserId().toString(), appTodoTaskDTO.getBusinessModuleId(), appTodoTaskDTO.getQueryType(),
                appTodoTaskDTO.getStatus(), appTodoTaskDTO.getType(), appTodoTaskDTO.getTitle()).getData();
    }

    @Override
    public IPage<TodoTaskView> findDoneTasks(Page page, AppTodoTaskDTO appTodoTaskDTO) {
        return terraceApi.loadDoneTask(page.getCurrent(), page.getSize(),
                appTodoTaskDTO.getCode(), appTodoTaskDTO.getTitle(), appTodoTaskDTO.getRealName(),
                SecurityUtils.getUser().getUserId().toString(), appTodoTaskDTO.getTaskName(), appTodoTaskDTO.getType(),
                appTodoTaskDTO.getBusinessModuleId()).getData();
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
        dto.setUserId(SecurityUtils.getUser().getUserId().toString());
        dto.setUserName(SecurityUtils.getUser().getUsername());
        dto.setTenantId("23");
        dto.setOrgId(SecurityUtils.getUser().getDeptIds().get(0));
        dto.setOrgName("jiangsu");
        dto.setDraft(false);
        dto.setProcessNodeDTO(task.getProcessNodeDTO());

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setId(task.getId());
        applicationDTO.setCode("xxx");
        applicationDTO.setModuleId("12");
        applicationDTO.setModuleName("oa");
        applicationDTO.setTitle(task.getTitle());
        applicationDTO.setCreator(SecurityUtils.getUser().getUsername());
        dto.setDto(applicationDTO);
        dto.setSuggestion(task.getSuggestion());

        log.info("StartProcessDTO : {}", dto);

        return terraceApi.start(dto).getData();
    }

    @Override
    public String submit(Task task) {
        SubmitProcessDTO dto = new SubmitProcessDTO();
        dto.setProcessDefinitionId(task.getProcessDefinitionId());
        dto.setCurrent(task.getCurrent());
        dto.setUserId(SecurityUtils.getUser().getUserId().toString());
        dto.setUserName(SecurityUtils.getUser().getUsername());
        dto.setTenantId("23");
        dto.setOrgId(SecurityUtils.getUser().getDeptIds().get(0));
        dto.setOrgName("jiangsu");
        dto.setTaskId(task.getTaskId());
        dto.setProcInstId(task.getProcInstId());
        dto.setProcessNodeDTO(task.getProcessNodeDTO());
        dto.setSuggestion(task.getSuggestion());

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setId(task.getId());
        applicationDTO.setCode("xxx");
        applicationDTO.setModuleId("12");
        applicationDTO.setModuleName("oa");
        applicationDTO.setTitle(task.getTitle());
        applicationDTO.setCreator(SecurityUtils.getUser().getUsername());
        dto.setDto(applicationDTO);

        return terraceApi.submit(dto).getData();
    }

    @Override
    public List<ProcessRouterView> findProcessRouter(String processDefinitionId, String taskDefKey) {
        return terraceApi.loadProcessRouter(taskDefKey, processDefinitionId).getData();
    }

    @Override
    public List<ProcessMessageView> loadMessage(String procInstId, List<String> taskDefKeyList, Integer orderRule) {
        return terraceApi.loadMessage(procInstId, taskDefKeyList, orderRule).getData();
    }

    @Override
    public Boolean sign(String taskId, String type) {
        return terraceApi.sign(taskId, type, SecurityUtils.getUser().getUserId().toString()).getData();
    }

    @Override
    public List<QueryCandidateView> loadCandidate(String processInstId, String taskDefKey, String taskId, Boolean back, String processDefId, String orgId) {
        return terraceApi.loadCandidate(processInstId, taskDefKey, taskId, back, processDefId, orgId).getData();
    }
}
