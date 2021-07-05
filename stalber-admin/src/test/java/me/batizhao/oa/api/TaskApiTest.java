package me.batizhao.oa.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.ims.domain.User;
import me.batizhao.oa.domain.Comment;
import me.batizhao.oa.domain.CommentAndTask;
import me.batizhao.oa.domain.Task;
import me.batizhao.terrace.dto.CandidateDTO;
import me.batizhao.terrace.dto.ProcessNodeDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
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
        Comment comment = new Comment().setTitle("ttt").setComment("ccc");

        ProcessNodeDTO processNodeDTO = new ProcessNodeDTO();
        processNodeDTO.setTarget("usertask2");
        processNodeDTO.setFlowName("南京发文流程");

        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setUserId("1");
        candidateDTO.setOrgId("2");
        processNodeDTO.setCandidate(asList(candidateDTO));

        List<ProcessNodeDTO> processNodeDTOList = asList(processNodeDTO);
        Task task = new Task().setProcessDefinitionId("jsoa_njfw:1:1292510")
                .setCurrent("usertask1").setId("1").setTitle("title")
                .setProcessNodeDTO(processNodeDTOList);

        CommentAndTask cat = new CommentAndTask().setTask(task).setComment(comment);

        mvc.perform(post("/oa/comment")
                .content(objectMapper.writeValueAsString(cat))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

}
