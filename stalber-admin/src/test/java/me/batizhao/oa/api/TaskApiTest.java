package me.batizhao.oa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.oa.domain.Invoice;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.dto.CandidateDTO;
import me.batizhao.terrace.dto.ProcessNodeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-03-18
 **/
@DirtiesContext
public class TaskApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenTask_whenProcessStart_thenSuccess() throws Exception {


        ProcessNodeDTO processNodeDTO = new ProcessNodeDTO();
        processNodeDTO.setTarget("usertask2");
        processNodeDTO.setFlowName("南京发文流程");

        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setUserId("1");
        candidateDTO.setOrgId("2");
        processNodeDTO.setCandidate(asList(candidateDTO));

        List<ProcessNodeDTO> processNodeDTOList = asList(processNodeDTO);
        Task task = new Task().setProcessDefinitionId("jsoa_sgkjfpsp:1:1292518")
                .setCurrent("zhuren").setId("1").setTitle("title")
                .setProcessNodeDTO(processNodeDTOList);

        Invoice invoice = new Invoice().setTitle("ttt").setCompany("ccc").setCompanyNumber("0123456").setTask(task);

        mvc.perform(post("/oa/invoice")
                .content(objectMapper.writeValueAsString(invoice))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

//    @Test
//    public void givenTask_whenProcessSubmit_thenSuccess() throws Exception {
//        ProcessNodeDTO processNodeDTO = new ProcessNodeDTO();
//        processNodeDTO.setTarget("eventend1");
//        processNodeDTO.setTargetName("送办结");
//        processNodeDTO.setFlowName("南京发文流程");
//
//        CandidateDTO candidateDTO = new CandidateDTO();
//        candidateDTO.setUserId("1");
//        candidateDTO.setOrgId("2");
//        processNodeDTO.setCandidate(asList(candidateDTO));
//
//        List<ProcessNodeDTO> processNodeDTOList = asList(processNodeDTO);
//        Task task = new Task().setProcessDefinitionId("jsoa_njfw:1:1292510")
//                .setCurrent("usertask2").setId("1").setTitle("title").setTaskId("1302551").setProcInstId("1302525")
//                .setProcessNodeDTO(processNodeDTOList);
//
//        mvc.perform(post("/oa/task")
//                .content(objectMapper.writeValueAsString(task))
//                .contentType(MediaType.APPLICATION_JSON)
//                .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
//    }

}
