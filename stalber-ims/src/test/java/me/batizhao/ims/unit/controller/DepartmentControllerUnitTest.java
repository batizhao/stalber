package me.batizhao.ims.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.ims.controller.DepartmentController;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.service.DepartmentLeaderService;
import me.batizhao.ims.service.DepartmentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 部门
 *
 * @author batizhao
 * @since 2021-04-25
 */
@WebMvcTest(DepartmentController.class)
public class DepartmentControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private DepartmentService departmentService;
    @MockBean
    private DepartmentLeaderService departmentLeaderService;

    private List<Department> departmentList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        departmentList = new ArrayList<>();
        departmentList.add(new Department(1, 0).setName("zhangsan"));
        departmentList.add(new Department(2, 1).setName("lisi"));
        departmentList.add(new Department(3, 2).setName("wangwu"));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindDepartment_thenSuccess() throws Exception {
        Long id = 1L;

        when(departmentService.findById(id)).thenReturn(departmentList.get(0));

        mvc.perform(get("/ims/department/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(departmentService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveDepartment_thenSuccess() throws Exception {
        Department requestBody = new Department().setName("zhaoliu");

        when(departmentService.saveOrUpdateDepartment(any(Department.class)))
                .thenReturn(departmentList.get(0));

        mvc.perform(post("/ims/department").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(departmentService).saveOrUpdateDepartment(any(Department.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateDepartment_thenSuccess() throws Exception {
        Department requestBody = new Department().setName("zhaoliu");

        when(departmentService.saveOrUpdateDepartment(any(Department.class)))
                .thenReturn(departmentList.get(1));

        mvc.perform(post("/ims/department").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(departmentService).saveOrUpdateDepartment(any(Department.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteDepartment_thenSuccess() throws Exception {
        when(departmentService.deleteById(anyInt())).thenReturn(true);

        mvc.perform(delete("/ims/department").param("id", "2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));

        verify(departmentService).deleteById(anyInt());
    }

    @Test
    @WithMockUser
    public void givenDepartment_whenUpdateStatus_thenSuccess() throws Exception {
        Department requestBody = new Department().setStatus("close");

        when(departmentService.updateStatus(any(Department.class))).thenReturn(true);

        mvc.perform(post("/ims/department/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(departmentService).updateStatus(any(Department.class));
    }
}
