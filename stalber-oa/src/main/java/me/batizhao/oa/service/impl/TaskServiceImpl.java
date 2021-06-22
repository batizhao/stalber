package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.batizhao.oa.domain.Task;
import me.batizhao.oa.service.TaskService;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.dto.*;
import me.batizhao.terrace.vo.TodoTaskView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Arrays.asList;

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
        dto.setQueryType("1");

        return terraceApi.loadTasks(dto).getData();
    }

    @Override
    public Object findById(Long id) {
        return terraceApi.loadTaskDetail(id.toString(), "0").getData();
    }

    @Override
    public Object submit(Task task) {
        SubmitProcessDTO dto = new SubmitProcessDTO();
        dto.setProcessDefinitionId("jsoa_njfw:1:1292510");
        dto.setCurrent("usertask1");
        dto.setUserId("1");
        dto.setUserName("admin");
        dto.setTenantId("23");
        dto.setOrgId("1");
        dto.setOrgName("jiangsu");
        dto.setTaskId(task.getTaskId());
        dto.setProcInstId(task.getProcInstId());

        ProcessNodeDTO processNodeDTO = new ProcessNodeDTO();
        processNodeDTO.setTarget("usertask2");
        processNodeDTO.setFlowName("南京发文流程");

        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setUserId("1");
        candidateDTO.setOrgId("2");
        processNodeDTO.setCandidate(asList(candidateDTO));

        List<ProcessNodeDTO> processNodeDTOList = asList(processNodeDTO);
        dto.setProcessNodeDTO(processNodeDTOList);

        ApplicationDTO applicationDTO = new ApplicationDTO();
        applicationDTO.setId("1");
        applicationDTO.setCode("xxx");
        applicationDTO.setModuleId("12");
        applicationDTO.setModuleName("oa");
        applicationDTO.setTitle(task.getTitle());
        applicationDTO.setCreator("admin");
        dto.setDto(applicationDTO);

        return terraceApi.submit(dto);
    }
}
