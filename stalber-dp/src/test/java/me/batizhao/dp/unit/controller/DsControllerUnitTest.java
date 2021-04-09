package me.batizhao.dp.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.dp.controller.DsController;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.service.DsService;
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
 * 数据源
 *
 * @author batizhao
 * @since 2020-10-19
 */
@WebMvcTest(DsController.class)
public class DsControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    DsService dsService;

    private List<Ds> dsList;
    private IPage<Ds> dsPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dsList = new ArrayList<>();
        dsList.add(new Ds().setId(1).setUsername("zhangsan"));
        dsList.add(new Ds().setId(2).setUsername("lisi"));
        dsList.add(new Ds().setId(3).setUsername("wangwu"));

        dsPageList = new Page<>();
        dsPageList.setRecords(dsList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindDs4Page_thenSuccess() throws Exception {
        when(dsService.findDss(any(Page.class), any(Ds.class))).thenReturn(dsPageList);

        mvc.perform(get("/dp/dss"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].username", equalTo("zhangsan")));

        verify(dsService).findDss(any(Page.class), any(Ds.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllDs_thenSuccess() throws Exception {
        when(dsService.list()).thenReturn(dsList);

        mvc.perform(get("/dp/ds"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));

        verify(dsService).list();
    }

    @Test
    @WithMockUser
    public void givenId_whenFindDs_thenSuccess() throws Exception {
        Integer id = 1;

        when(dsService.findById(id)).thenReturn(dsList.get(0));

        mvc.perform(get("/dp/ds/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.username").value("zhangsan"));

        verify(dsService).findById(anyInt());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveDs_thenSuccess() throws Exception {
        Ds requestBody = new Ds().setUsername("zhaoliu");

        when(dsService.saveOrUpdateDs(any(Ds.class)))
                .thenReturn(dsList.get(0));

        mvc.perform(post("/dp/ds").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(dsService).saveOrUpdateDs(any(Ds.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateDs_thenSuccess() throws Exception {
        Ds requestBody = new Ds().setId(2).setUsername("zhaoliu");

        when(dsService.saveOrUpdateDs(any(Ds.class)))
                .thenReturn(dsList.get(1));

        mvc.perform(post("/dp/ds").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(dsService).saveOrUpdateDs(any(Ds.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteDs_thenSuccess() throws Exception {
        when(dsService.removeByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/dp/ds").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(dsService).removeByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenDs_whenUpdateStatus_thenSuccess() throws Exception {
        Ds requestBody = new Ds().setId(2).setStatus("close");

        when(dsService.updateStatus(any(Ds.class))).thenReturn(true);

        mvc.perform(post("/dp/ds/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(dsService).updateStatus(any(Ds.class));
    }
}
