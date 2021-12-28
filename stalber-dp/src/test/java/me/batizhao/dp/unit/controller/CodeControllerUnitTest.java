package me.batizhao.dp.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.dp.controller.CodeController;
import me.batizhao.dp.domain.Code;
import me.batizhao.dp.service.CodeMetaService;
import me.batizhao.dp.service.CodeService;
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
 * @author batizhao
 * @date 2020/10/13
 */
@WebMvcTest(CodeController.class)
public class CodeControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    CodeService codeService;
    @MockBean
    private CodeMetaService codeMetaService;

    private List<Code> codeList;
    private IPage<Code> codePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        codeList = new ArrayList<>();
        codeList.add(new Code().setId(1L).setTableName("zhangsan"));
        codeList.add(new Code().setId(2L).setTableName("lisi"));
        codeList.add(new Code().setId(3L).setTableName("wangwu"));

        codePageList = new Page<>();
        codePageList.setRecords(codeList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindCodes_thenSuccess() throws Exception {
        when(codeService.findCodes(any(Page.class), any(Code.class))).thenReturn(codePageList);

        mvc.perform(get("/dp/codes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].tableName", equalTo("zhangsan")));

        verify(codeService).findCodes(any(Page.class), any(Code.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindCode_thenSuccess() throws Exception {
        Long id = 1L;

        when(codeService.findById(id)).thenReturn(codeList.get(0));

        mvc.perform(get("/dp/code/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.code.tableName").value("zhangsan"));

        verify(codeService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveCode_thenSuccess() throws Exception {
        Code requestBody = new Code().setTableName("zhaoliu");

        when(codeService.saveOrUpdateCode(any(Code.class)))
                .thenReturn(codeList.get(0));

        mvc.perform(post("/dp/code").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(codeService).saveOrUpdateCode(any(Code.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateCode_thenSuccess() throws Exception {
        Code requestBody = new Code().setId(2L).setTableName("zhaoliu");

        when(codeService.saveOrUpdateCode(any(Code.class)))
                .thenReturn(codeList.get(1));

        mvc.perform(post("/dp/code").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(codeService).saveOrUpdateCode(any(Code.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteCode_thenSuccess() throws Exception {
        when(codeService.deleteByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/dp/code").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(codeService).deleteByIds(anyList());
    }

//    @Test
//    @WithMockUser
//    public void givenConfig_whenGenerateCode_thenSuccess() throws Exception {
//        byte[] data = new byte[]{80, 75, 3, 4, 20, 0, 8, 8, 8, 0, 107, 78, 77, 81, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
//                64, 0, 0, 0, 112, 101, 99, 97, 100, 111, 47, 115, 114, 99, 47, 109, 97, 105, 110, 47, 106, 97, 118, 97,
//                47, 109, 101, 47, 98, 97, 116, 105, 122, 104, 97, 111, 47, 103, 101, 110, 101, 114, 97, 116, 111, 114,
//                47, 109, 97, 112, 112, 101, 114, 47, 76, 111, 103, 77, 97, 112, 112, 101, 114, 46, 106, 97, 118, 97, 109,
//                79, 75, 78, 3, 49, 12, -35, -5, 20, 94, -113, 84, 119, 58, -88, 11, 10, 66, 21, -21, 114, 8, 55, 99, 102,
//                34, -102, 56, 74, 50, 18, 31, 113, -114, -82, 89, 84, -30, 22, -100, -90, 21, -57, 32, 109, -89, -80, 65,
//                -14, -62, -49, -17, 99, 59, -80, 121, -30, 78, -48, 9, -83, 57, -37, -41, -98, -107, 58, -15, 18, 57, 107,
//                36, -57, 33, 72, -68, 1, -80, 46, 104, -52, 104, -44, 21, -103, 58, -37, -22, 64, -18, -27, -24, 72, 97,
//                51, 36, 50, 26, 101, 84, -45, 61, 39, 121, 24, -115, -93, -17, -1, -12, 86, 29, 91, 79, 43, -19, 126,
//                -123, 26, 59, -30, -64, -90, 23, -78, -89, 116, 98, -17, 53, -105, 78, 125, -94, 75, 42, 76, -85, 10,
//                -80, -62, -61, 118, -73, -1, -38, 126, 127, 124, 22, 112, -60, 75, 30, 114, -81, 17, 47, -69, 78, -77,
//                100, -67, 17, 108, -22, -90, -98, -52, 74, 93, 97, 125, -67, -104, -49, 22, 77, 83, -40, 41, 44, -49,
//                -103, 16, -122, -11, -58, 26, -76, 62, 75, 124, -28, 98, 40, 87, -99, 41, -108, -25, 44, -66, 77, -8,
//                -9, -41, 109, 33, -17, -16, 13, -32, 29, 126, 0, 80, 75, 7, 8, 115, -6, 24, 78, -39, 0, 0, 0, 64, 1, 0,
//                0, 80, 75, 1, 2, 20, 0, 20, 0, 8, 8, 8, 0, 107, 78, 77, 81, 115, -6, 24, 78, -39, 0, 0, 0, 64, 1, 0, 0,
//                64, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 112, 101, 99, 97, 100, 111, 47, 115, 114, 99, 47,
//                109, 97, 105, 110, 47, 106, 97, 118, 97, 47, 109, 101, 47, 98, 97, 116, 105, 122, 104, 97, 111, 47, 103,
//                101, 110, 101, 114, 97, 116, 111, 114, 47, 109, 97, 112, 112, 101, 114, 47, 76, 111, 103, 77, 97, 112,
//                112, 101, 114, 46, 106, 97, 118, 97, 80, 75, 5, 6, 0, 0, 0, 0, 1, 0, 1, 0, 110, 0, 0, 0, 71, 1, 0, 0, 0, 0};
//
//        GenConfig requestBody = new GenConfig().setTableName("log").setAuthor("batizhao").setComments("comment")
//                .setModuleName("system").setPackageName("me.batizhao");
//
//        doReturn(data).when(codeService).generateCode(any(GenConfig.class));
//
//        mvc.perform(post("/code").with(csrf())
//                .content(objectMapper.writeValueAsString(requestBody))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Length", "459"));
//    }
}
