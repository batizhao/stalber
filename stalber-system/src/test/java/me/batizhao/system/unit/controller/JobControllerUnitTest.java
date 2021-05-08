package me.batizhao.system.unit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.system.controller.JobController;
import me.batizhao.system.domain.SysJob;
import me.batizhao.system.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 任务调度
 *
 * @author batizhao
 * @since 2021-05-07
 */
@WebMvcTest(JobController.class)
public class JobControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    JobService jobService;

    private List<SysJob> jobList;
    private Page<SysJob> jobPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        jobList = new ArrayList<>();
        jobList.add(new SysJob().setId(1L).setName("zhangsan"));
        jobList.add(new SysJob().setId(2L).setName("lisi"));
        jobList.add(new SysJob().setId(3L).setName("wangwu"));

        jobPageList = new Page<>();
        jobPageList.setRecords(jobList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindJobs_thenSuccess() throws Exception {
        when(jobService.findJobs(any(Page.class), any(SysJob.class))).thenReturn(jobPageList);

        mvc.perform(get("/system/jobs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(jobService).findJobs(any(Page.class), any(SysJob.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllJob_thenSuccess() throws Exception {
        when(jobService.findJobs(any(SysJob.class))).thenReturn(jobList);

        mvc.perform(get("/system/job"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].name", equalTo("zhangsan")));

        verify(jobService).findJobs(any(SysJob.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindJob_thenSuccess() throws Exception {
        Long id = 1L;

        when(jobService.findById(id)).thenReturn(jobList.get(0));

        mvc.perform(get("/system/job/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(jobService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveJob_thenSuccess() throws Exception {
        SysJob requestBody = new SysJob().setName("zhaoliu");

        when(jobService.saveOrUpdateJob(any(SysJob.class)))
                .thenReturn(jobList.get(0));

        mvc.perform(post("/system/job").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(jobService).saveOrUpdateJob(any(SysJob.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateJob_thenSuccess() throws Exception {
        SysJob requestBody = new SysJob().setId(2L).setName("zhaoliu");

        when(jobService.saveOrUpdateJob(any(SysJob.class)))
                .thenReturn(jobList.get(1));

        mvc.perform(post("/system/job").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(jobService).saveOrUpdateJob(any(SysJob.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteJob_thenSuccess() throws Exception {
        when(jobService.removeByIds(anyList())).thenReturn(true);
        mvc.perform(delete("/system/job").param("ids", "1,2").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
            .andExpect(jsonPath("$.data").value(true));

        verify(jobService).removeByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenJob_whenUpdateStatus_thenSuccess() throws Exception {
        SysJob requestBody = new SysJob().setId(2L).setStatus("close");

        when(jobService.updateStatus(any(SysJob.class))).thenReturn(true);

        mvc.perform(post("/system/job/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(jobService).updateStatus(any(SysJob.class));
    }
}
