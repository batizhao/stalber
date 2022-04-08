package me.batizhao.system.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.system.controller.DictionaryController;
import me.batizhao.system.domain.Dictionary;
import me.batizhao.system.service.DictionaryService;
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
@WebMvcTest(DictionaryController.class)
public class DictionaryControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    DictionaryService dictTypeService;

    private List<Dictionary> dictionaryList;
    private IPage<Dictionary> dictTypePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        dictionaryList = new ArrayList<>();
        dictionaryList.add(new Dictionary().setId(1L).setName("zhangsan"));
        dictionaryList.add(new Dictionary().setId(2L).setName("lisi"));
        dictionaryList.add(new Dictionary().setId(3L).setName("wangwu"));

        dictTypePageList = new Page<>();
        dictTypePageList.setRecords(dictionaryList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindDictTypes_thenSuccess() throws Exception {
        when(dictTypeService.findDictionaries(any(Page.class), any(Dictionary.class))).thenReturn(dictTypePageList);

        mvc.perform(get("/system/dict/types"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(dictTypeService).findDictionaries(any(Page.class), any(Dictionary.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllDictType_thenSuccess() throws Exception {
        when(dictTypeService.list()).thenReturn(dictionaryList);

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

        when(dictTypeService.findById(id)).thenReturn(dictionaryList.get(0));

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
        Dictionary requestBody = new Dictionary().setName("zhaoliu");

        when(dictTypeService.saveOrUpdateDictionary(any(Dictionary.class)))
                .thenReturn(dictionaryList.get(0));

        mvc.perform(post("/system/dict/type").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(dictTypeService).saveOrUpdateDictionary(any(Dictionary.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateDictType_thenSuccess() throws Exception {
        Dictionary requestBody = new Dictionary().setId(2L).setName("zhaoliu");

        when(dictTypeService.saveOrUpdateDictionary(any(Dictionary.class)))
                .thenReturn(dictionaryList.get(1));

        mvc.perform(post("/system/dict/type").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(dictTypeService).saveOrUpdateDictionary(any(Dictionary.class));
    }

//    @Test
//    @WithMockUser
//    public void givenId_whenDeleteDictType_thenSuccess() throws Exception {
//        when(dictTypeService.deleteByIds(anyList())).thenReturn(true);
//
//        mvc.perform(delete("/system/dict/type").param("codes", "1,2").with(csrf()))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data").value(true));
//
//        verify(dictTypeService).deleteByIds(anyList());
//    }

    @Test
    @WithMockUser
    public void givenDs_whenUpdateStatus_thenSuccess() throws Exception {
        Dictionary requestBody = new Dictionary().setId(2L).setStatus("close");

        when(dictTypeService.updateStatus(any(Dictionary.class))).thenReturn(true);

        mvc.perform(post("/system/dict/type/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(dictTypeService).updateStatus(any(Dictionary.class));
    }
}
