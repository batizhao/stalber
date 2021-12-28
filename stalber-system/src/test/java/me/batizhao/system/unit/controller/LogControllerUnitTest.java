package me.batizhao.system.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.system.controller.LogController;
import me.batizhao.system.domain.Log;
import me.batizhao.system.service.LogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.text.StringContainsInOrder.stringContainsInOrder;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(LogController.class)
public class LogControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private LogService logService;

    private List<Log> logList;
    private IPage<Log> logPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        logList = new ArrayList<>();
        logList.add(new Log().setId(1L).setClassMethod("handleMenuTree4Me").setClassName("me.batizhao.ims.web.MenuController"));
        logList.add(new Log().setId(2L).setClassMethod("handleUserInfo").setClassName("me.batizhao.ims.web.UserController"));

        logPageList = new Page<>();
        logPageList.setRecords(logList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindLogs_thenSuccess() throws Exception {
        when(logService.findLogs(any(Page.class), any(Log.class))).thenReturn(logPageList);

        mvc.perform(get("/system/logs"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("handleMenuTree4Me", "handleUserInfo")))
                .andExpect(jsonPath("$.data.records", hasSize(2)))
                .andExpect(jsonPath("$.data.records[1].classMethod", equalTo("handleUserInfo")));

        verify(logService).findLogs(any(Page.class), any(Log.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindLog_thenSuccess() throws Exception {
        Long id = 1L;

        when(logService.findById(id)).thenReturn(logList.get(0));

        mvc.perform(get("/system/log/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.classMethod").value("handleMenuTree4Me"));

        verify(logService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveLog_thenSuccess() throws Exception {
        Log requestBody = new Log().setDescription("根据用户ID查询角色").setSpend(20).setClassMethod("findRolesByUserId")
                .setClassName("me.batizhao.ims.web.RoleController").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreateTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");


        when(logService.save(any(Log.class)))
                .thenReturn(true);

        mvc.perform(post("/system/log").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", equalTo(true)));

        verify(logService).save(any(Log.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteLog_thenSuccess() throws Exception {
        when(logService.removeByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/system/log").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(logService).removeByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteAllLog_thenSuccess() throws Exception {
        when(logService.remove(null)).thenReturn(true);

        mvc.perform(delete("/system/log").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(logService).remove(null);
    }
}