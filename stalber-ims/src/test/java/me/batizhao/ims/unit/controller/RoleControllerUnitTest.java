package me.batizhao.ims.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.ims.controller.RoleController;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
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
 * @since 2020-02-10
 */
@WebMvcTest(RoleController.class)
public class RoleControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    RoleService roleService;
    @MockBean
    RoleMenuService roleMenuService;

    private List<Role> roleList;
    private IPage<Role> rolePageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setName("zhangsan"));
        roleList.add(new Role().setId(2L).setName("lisi"));
        roleList.add(new Role().setId(3L).setName("wangwu"));

        rolePageList = new Page<>();
        rolePageList.setRecords(roleList);
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindRoles_thenSuccess() throws Exception {
        when(roleService.findRoles(any(Page.class), any(Role.class))).thenReturn(rolePageList);

        mvc.perform(get("/ims/roles"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].name", equalTo("zhangsan")));

        verify(roleService).findRoles(any(Page.class), any(Role.class));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllRole_thenSuccess() throws Exception {
        when(roleService.list()).thenReturn(roleList);

        mvc.perform(get("/ims/role"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data", hasSize(3)))
                .andExpect(jsonPath("$.data[0].name", equalTo("zhangsan")));

        verify(roleService).list();
    }

    @Test
    @WithMockUser
    public void givenId_whenFindRole_thenSuccess() throws Exception {
        Long id = 1L;

        when(roleService.findById(id)).thenReturn(roleList.get(0));

        mvc.perform(get("/ims/role/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("zhangsan"));

        verify(roleService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveRole_thenSuccess() throws Exception {
        Role requestBody = new Role().setName("zhaoliu").setCode("xxx");

        when(roleService.saveOrUpdateRole(any(Role.class)))
                .thenReturn(roleList.get(0));

        mvc.perform(post("/ims/role").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(roleService).saveOrUpdateRole(any(Role.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateRole_thenSuccess() throws Exception {
        Role requestBody = new Role().setId(2L).setName("zhaoliu").setCode("xxx");

        when(roleService.saveOrUpdateRole(any(Role.class)))
                .thenReturn(roleList.get(1));

        mvc.perform(post("/ims/role").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)));

        verify(roleService).saveOrUpdateRole(any(Role.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteRole_thenSuccess() throws Exception {
        when(roleService.deleteByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/ims/role").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(roleService).deleteByIds(anyList());
    }

    @Test
    @WithMockUser
    public void givenRole_whenUpdateStatus_thenSuccess() throws Exception {
        Role requestBody = new Role().setId(2L).setStatus("close");

        when(roleService.updateStatus(any(Role.class))).thenReturn(true);

        mvc.perform(post("/ims/role/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(roleService).updateStatus(any(Role.class));
    }

    @Test
    @WithMockUser
    public void giveRoleMenus_whenAdd_thenSuccess() throws Exception {
        List<RoleMenu> roleMenuList = new ArrayList<>();
        roleMenuList.add(new RoleMenu().setRoleId(1L).setMenuId(1L));
        roleMenuList.add(new RoleMenu().setRoleId(1L).setMenuId(2L));

        doReturn(true).when(roleMenuService).updateRoleMenus(any(List.class));

        mvc.perform(post("/ims/role/menu").with(csrf())
                .content(objectMapper.writeValueAsString(roleMenuList))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }
}