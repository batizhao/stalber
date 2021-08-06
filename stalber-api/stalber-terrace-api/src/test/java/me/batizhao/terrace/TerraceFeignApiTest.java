package me.batizhao.terrace;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.config.ThirdPartyClientConfig;
import me.batizhao.terrace.config.ThirdPartyServiceProperties;
import me.batizhao.terrace.dto.*;
import me.batizhao.terrace.vo.InitProcessDefView;
import me.batizhao.terrace.vo.ProcessRouterView;
import me.batizhao.terrace.vo.TaskNodeView;
import me.batizhao.terrace.vo.TodoTaskView;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author batizhao
 * @date 2021/6/17
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Tag("api")
@Import(ThirdPartyClientConfig.class)
@EnableConfigurationProperties(value = ThirdPartyServiceProperties.class)
@TestPropertySource(properties = {"pecado.third-party.enabled=true",
        "pecado.third-party.terrace-service-url=http://172.31.21.208:8886/terrace/",
        "pecado.third-party.token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6IjE2MjgyNTA3MTA2NzkiLCJleHAiOjE2MjgyOTM5MTAsImFjY291bnQiOiJqc29hIn0.P8L8C8vTLSAP4qJbGNMKu3ceTv1owJh7O_t29tkdppM"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TerraceFeignApiTest {

    @Autowired
    private TerraceApi terraceApi;

    @Autowired
    private ThirdPartyServiceProperties thirdPartyServiceProperties;

    private static String taskId;
    private static String procInstId;

    @Test
    @Order(1)
    public void testServiceUrl() {
        log.info("Flowable service url: {}", thirdPartyServiceProperties.getTerraceServiceUrl());

        assertThat(thirdPartyServiceProperties.getTerraceServiceUrl(), equalTo("http://172.31.21.208:8886/terrace/"));
    }

    @Test
    public void givenKey_whenLoadProcessDefinition_thenSuccess() {
        R<InitProcessDefView> result = terraceApi.loadProcessDefinitionByKey("jsoa_njfw");

        assertThat(result.getCode(), equalTo("000000"));

        InitProcessDefView view = result.getData();
        log.info("View: {}, Dto: {}", view.getView(), view.getDto());
        assertThat(view.getView().getConfig().getId(), equalTo("9f9e4129cab18ed44a1b313d87a52d0b"));

        log.info("Config: {}", view.getView().getConfig().getConfig());
        assertThat(view.getView().getConfig().getConfig().getGlobal().isNeed(), equalTo(true));
        assertThat(view.getView().getConfig().getConfig().getForm().getPcPath(), equalTo("60d1918964549dabd95cff87"));
    }

    @Test
    public void given_whenProcessStart_thenSuccess() {
        StartProcessDTO dto = new StartProcessDTO();
        dto.setProcessDefinitionId("jsoa_njfw:1:1292510");
        dto.setCurrent("usertask1");
        dto.setUserId("1");
        dto.setUserName("admin");
        dto.setTenantId("23");
        dto.setOrgId("1");
        dto.setOrgName("jiangsu");
        dto.setDraft(false);

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
        applicationDTO.setTitle("title");
        applicationDTO.setCreator("admin");
        dto.setDto(applicationDTO);

        R<String> result = terraceApi.start(dto);

        assertThat(result.getCode(), equalTo("000000"));
        assertThat(result.getData(), notNullValue());
    }

    @Test
    @Order(2)
    public void given_whenLoadTasks_thenSuccess() {
        AppTodoTaskDTO dto = new AppTodoTaskDTO();
        dto.setBusinessModuleId("12");
        dto.setUserName("1");
        dto.setQueryType("1");

        R<Page<TodoTaskView>> result = terraceApi.loadTasks(dto);

        assertThat(result.getCode(), equalTo("000000"));
        assertThat(result.getData().getTotal(), greaterThan(0L));
        assertThat(result.getData().getRecords().get(0).getTaskId(), notNullValue());
        assertThat(result.getData().getRecords().get(0).getProcInstId(), notNullValue());

        taskId = result.getData().getRecords().get(0).getTaskId();
        procInstId = result.getData().getRecords().get(0).getProcInstId();
    }

    @Test
    public void given_whenLoadTaskDetail_thenSuccess() {
        R<TaskNodeView> result = terraceApi.loadTaskDetail("1300307", "0");

        assertThat(result.getCode(), equalTo("000000"));
        assertThat(result.getData().getConfig().getId(), equalTo("b83ada88e698c454f8a645439024bf73"));
    }

    /**
     * 这里要控制在 given_whenLoadTasks_thenSuccess 执行以后运行
     */
    @Test
    @Order(5)
    public void given_whenProcessSubmit_thenSuccess() {
        SubmitProcessDTO dto = new SubmitProcessDTO();
        dto.setProcessDefinitionId("jsoa_njfw:1:1292510");
        dto.setCurrent("usertask1");
        dto.setUserId("1");
        dto.setUserName("admin");
        dto.setTenantId("23");
        dto.setOrgId("1");
        dto.setOrgName("jiangsu");
        dto.setTaskId(taskId);
        dto.setProcInstId(procInstId);

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
        applicationDTO.setTitle("title");
        applicationDTO.setCreator("admin");
        dto.setDto(applicationDTO);

        R<String> result = terraceApi.submit(dto);

        assertThat(result.getCode(), equalTo("000000"));
        assertThat(result.getData(), equalTo("true"));
    }

    @Test
    void givenTaskDef_whenLoadProcessRouter_thenSuccess() {
        R<List<ProcessRouterView>> result = terraceApi.loadProcessRouter("usertask1", "jsoa_njfw:1:1292510");

        assertThat(result.getCode(), equalTo("000000"));
        assertThat(result.getData().get(0).getName(), equalTo("送部门审核"));
    }
}
