package me.batizhao.system.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.system.controller.DictTypeController;
import me.batizhao.system.domain.DictType;
import me.batizhao.system.service.DictTypeService;
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
 * 字典类型
 *
 * @author batizhao
 * @since 2021-02-07
 */
@WebMvcTest(DictTypeController.class)
public class DictTypeControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    DictTypeService dictTypeService;

    private List<DictType> dictTypeList;
    private IPage<DictType> dictTypePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dictTypeList = new ArrayList<>();
        dictTypeList.add(new DictType().setId(1L).setName("zhangsan"));
        dictTypeList.add(new DictType().setId(2L).setName("lisi"));
        dictTypeList.add(new DictType().setId(3L).setName("wangwu"));

        dictTypePageList = new Page<>();
        dictTypePageList.setRecords(dictTypeList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindDictTypes_thenSuccess() throws Exception {
        when(dictTypeService.findDictTypes(any(Page.class), any(DictType.class))).thenReturn(dictTypePageList);

        mvc.perform(get("/system/dict/types"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(dictTypeService).findDictTypes(any(Page.class), any(DictType.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllDictType_thenSuccess() throws Exception {
        when(dictTypeService.list()).thenReturn(dictTypeList);

        mvc.perform(get("/system/dict/type"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].name", equalTo("zhangsan")));

        verify(dictTypeService).list();
    }

    @Test
    @WithMockUser
    public void givenId_whenFindDictType_thenSuccess() throws Exception {
        Long id = 1L;

        when(dictTypeService.findById(id)).thenReturn(dictTypeList.get(0));

        mvc.perform(get("/system/dict/type/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(dictTypeService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveDictType_thenSuccess() throws Exception {
        DictType requestBody = new DictType().setName("zhaoliu");

        when(dictTypeService.saveOrUpdateDictType(any(DictType.class)))
                .thenReturn(dictTypeList.get(0));

        mvc.perform(post("/system/dict/type").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(dictTypeService).saveOrUpdateDictType(any(DictType.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateDictType_thenSuccess() throws Exception {
        DictType requestBody = new DictType().setId(2L).setName("zhaoliu");

        when(dictTypeService.saveOrUpdateDictType(any(DictType.class)))
                .thenReturn(dictTypeList.get(1));

        mvc.perform(post("/system/dict/type").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(dictTypeService).saveOrUpdateDictType(any(DictType.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteDictType_thenSuccess() throws Exception {
        when(dictTypeService.deleteByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/system/dict/type").param("codes", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(dictTypeService).deleteByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenDs_whenUpdateStatus_thenSuccess() throws Exception {
        DictType requestBody = new DictType().setId(2L).setStatus("close");

        when(dictTypeService.updateStatus(any(DictType.class))).thenReturn(true);

        mvc.perform(post("/system/dict/type/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(dictTypeService).updateStatus(any(DictType.class));
    }
}
