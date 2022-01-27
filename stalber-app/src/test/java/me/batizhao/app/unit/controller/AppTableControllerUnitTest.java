package me.batizhao.app.unit.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.app.controller.AppTableController;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.service.AppTableService;
import me.batizhao.common.core.constant.ResultEnum;
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
 * 应用表
 *
 * @author batizhao
 * @since 2022-01-27
 */
@WebMvcTest(AppTableController.class)
public class AppTableControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    AppTableService appTableService;

    private List<AppTable> appTableList;
    private Page<AppTable> appTablePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        appTableList = new ArrayList<>();
        appTableList.add(new AppTable().setId(1L).setTableName("zhangsan"));
        appTableList.add(new AppTable().setId(2L).setTableName("lisi"));
        appTableList.add(new AppTable().setId(3L).setTableName("wangwu"));

        appTablePageList = new Page<>();
        appTablePageList.setRecords(appTableList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAppTables_thenSuccess() throws Exception {
        when(appTableService.findAppTables(any(Page.class), any(AppTable.class))).thenReturn(appTablePageList);

        mvc.perform(get("/app/tables"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].tableName", equalTo("zhangsan")));

        verify(appTableService).findAppTables(any(Page.class), any(AppTable.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllAppTable_thenSuccess() throws Exception {
        when(appTableService.findAppTables(any(AppTable.class))).thenReturn(appTableList);

        mvc.perform(get("/app/table"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].tableName", equalTo("zhangsan")));

        verify(appTableService).findAppTables(any(AppTable.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindAppTable_thenSuccess() throws Exception {
        Long id = 1L;

        when(appTableService.findById(id)).thenReturn(appTableList.get(0));

        mvc.perform(get("/app/table/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.tableName").value("zhangsan"));

        verify(appTableService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveAppTable_thenSuccess() throws Exception {
        when(appTableService.saveOrUpdateAppTable(anyList()))
                .thenReturn(true);

        mvc.perform(post("/app/table").with(csrf())
                .content(objectMapper.writeValueAsString(appTableList))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", equalTo(true)));

        verify(appTableService).saveOrUpdateAppTable(anyList());
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateAppTable_thenSuccess() throws Exception {
        when(appTableService.saveOrUpdateAppTable(anyList()))
                .thenReturn(true);

        mvc.perform(post("/app/table").with(csrf())
                .content(objectMapper.writeValueAsString(appTableList))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", equalTo(true)));

        verify(appTableService).saveOrUpdateAppTable(anyList());
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteAppTable_thenSuccess() throws Exception {
        when(appTableService.removeByIds(anyList())).thenReturn(true);
        mvc.perform(delete("/app/table").param("ids", "1,2").with(csrf()))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
            .andExpect(jsonPath("$.data").value(true));

        verify(appTableService).removeByIds(anyList());
    }

}
