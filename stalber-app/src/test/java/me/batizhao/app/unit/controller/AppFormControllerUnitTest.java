package me.batizhao.app.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.app.controller.AppFormController;
import me.batizhao.app.domain.AppForm;
import me.batizhao.app.service.AppFormService;
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
 * 应用表单
 *
 * @author batizhao
 * @since 2022-02-24
 */
@WebMvcTest(AppFormController.class)
public class AppFormControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    AppFormService appFormService;

    private List<AppForm> appFormList;
    private Page<AppForm> appFormPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        appFormList = new ArrayList<>();
        appFormList.add(new AppForm().setId(1L).setName("zhangsan"));
        appFormList.add(new AppForm().setId(2L).setName("lisi"));
        appFormList.add(new AppForm().setId(3L).setName("wangwu"));

        appFormPageList = new Page<>();
        appFormPageList.setRecords(appFormList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAppForms_thenSuccess() throws Exception {
        when(appFormService.findAppForms(any(Page.class), any(AppForm.class))).thenReturn(appFormPageList);

        mvc.perform(get("/app/forms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(appFormService).findAppForms(any(Page.class), any(AppForm.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllAppForm_thenSuccess() throws Exception {
        when(appFormService.findAppForms(any(AppForm.class))).thenReturn(appFormList);

        mvc.perform(get("/app/form"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].name", equalTo("zhangsan")));

        verify(appFormService).findAppForms(any(AppForm.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindAppForm_thenSuccess() throws Exception {
        Long id = 1L;

        when(appFormService.findById(id)).thenReturn(appFormList.get(0));

        mvc.perform(get("/app/form/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(appFormService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveAppForm_thenSuccess() throws Exception {
        AppForm requestBody = new AppForm().setName("zhaoliu");

        when(appFormService.saveOrUpdateAppForm(any(AppForm.class)))
                .thenReturn(appFormList.get(0));

        mvc.perform(post("/app/form").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(appFormService).saveOrUpdateAppForm(any(AppForm.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateAppForm_thenSuccess() throws Exception {
        AppForm requestBody = new AppForm().setId(2L).setName("zhaoliu");

        when(appFormService.saveOrUpdateAppForm(any(AppForm.class)))
                .thenReturn(appFormList.get(1));

        mvc.perform(post("/app/form").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(appFormService).saveOrUpdateAppForm(any(AppForm.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteAppForm_thenSuccess() throws Exception {
        when(appFormService.removeByIds(anyList())).thenReturn(true);
        mvc.perform(delete("/app/form").param("ids", "1,2").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
            .andExpect(jsonPath("$.data").value(true));

        verify(appFormService).removeByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenAppForm_whenUpdateStatus_thenSuccess() throws Exception {
        AppForm requestBody = new AppForm().setId(2L).setStatus("close");

        when(appFormService.updateStatus(any(AppForm.class))).thenReturn(true);

        mvc.perform(post("/app/form/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(appFormService).updateStatus(any(AppForm.class));
    }
}
