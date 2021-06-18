package me.batizhao.terrace;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.config.ThirdPartyClientConfig;
import me.batizhao.terrace.config.ThirdPartyServiceProperties;
import me.batizhao.terrace.dto.*;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static java.util.Arrays.asList;

/**
 * @author batizhao
 * @date 2021/6/17
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Tag("api")
@Import(ThirdPartyClientConfig.class)
@EnableConfigurationProperties(value = ThirdPartyServiceProperties.class)
@TestPropertySource(properties = {"pecado.third-party.enabled=true", "pecado.third-party.terrace-service-url=http://172.31.21.208:8886/terrace/"})
@Slf4j
public class TerraceFeignApiTest {

    @Autowired
    private TerraceApi terraceApi;

    @Autowired
    private ThirdPartyServiceProperties thirdPartyServiceProperties;

    @Test
    public void testServiceUrl() {
        log.info("Flowable service url: {}", thirdPartyServiceProperties.getTerraceServiceUrl());
    }

    @Test
    public void givenKey_whenLoadProcessDefinition_thenSuccess() {
        terraceApi.loadProcessDefinitionByKey("jsoa_njfw");
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

        terraceApi.start(dto);
    }

    @Test
    public void given_whenLoadTasks_thenSuccess() {
        AppTodoTaskDTO dto = new AppTodoTaskDTO();
        dto.setBusinessModuleId("12");
        dto.setUserName("1");

        terraceApi.loadTasks(dto);
    }

    @Test
    public void given_whenLoadTaskDetail_thenSuccess() {
        terraceApi.loadTaskDetail("1297527", "0");
    }

    @Test
    public void given_whenProcessSubmit_thenSuccess() {
        SubmitProcessDTO dto = new SubmitProcessDTO();
        dto.setProcessDefinitionId("jsoa_njfw:1:1292510");
        dto.setCurrent("usertask1");
        dto.setUserId("1");
        dto.setUserName("admin");
        dto.setTenantId("23");
        dto.setOrgId("1");
        dto.setOrgName("jiangsu");
        dto.setTaskId("1297527");
        dto.setProcInstId("1297501");

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

        terraceApi.submit(dto);
    }
}
