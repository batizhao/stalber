package me.batizhao.dp.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.dp.controller.FormController;
import me.batizhao.dp.domain.Form;
import me.batizhao.dp.service.FormService;
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
 * 表单
 *
 * @author batizhao
 * @since 2021-03-08
 */
@WebMvcTest(FormController.class)
public class FormControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    FormService formService;

    private List<Form> formList;
    private IPage<Form> formPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        formList = new ArrayList<>();
        formList.add(new Form().setId(1L).setName("zhangsan"));
        formList.add(new Form().setId(2L).setName("lisi"));
        formList.add(new Form().setId(3L).setName("wangwu"));

        formPageList = new Page<>();
        formPageList.setRecords(formList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindForms_thenSuccess() throws Exception {
        when(formService.findForms(any(Page.class), any(Form.class))).thenReturn(formPageList);

        mvc.perform(get("/dp/forms"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(formService).findForms(any(Page.class), any(Form.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindForm_thenSuccess() throws Exception {
        Long id = 1L;

        when(formService.findById(id)).thenReturn(formList.get(0));

        mvc.perform(get("/dp/form/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(formService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveForm_thenSuccess() throws Exception {
        Form requestBody = new Form().setName("zhaoliu");

        when(formService.saveOrUpdateForm(any(Form.class)))
                .thenReturn(formList.get(0));

        mvc.perform(post("/dp/form").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(formService).saveOrUpdateForm(any(Form.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateForm_thenSuccess() throws Exception {
        Form requestBody = new Form().setId(2L).setName("zhaoliu");

        when(formService.saveOrUpdateForm(any(Form.class)))
                .thenReturn(formList.get(1));

        mvc.perform(post("/dp/form").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(formService).saveOrUpdateForm(any(Form.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteForm_thenSuccess() throws Exception {
        when(formService.removeByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/dp/form").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(formService).removeByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenForm_whenUpdateStatus_thenSuccess() throws Exception {
        Form requestBody = new Form().setId(2L).setStatus("close");

        when(formService.updateStatus(any(Form.class))).thenReturn(true);

        mvc.perform(post("/dp/form/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(formService).updateStatus(any(Form.class));
    }
}
