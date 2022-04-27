package me.batizhao.ims.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.common.core.domain.PecadoUser;
import me.batizhao.common.core.domain.TreeNode;
import me.batizhao.common.core.util.SecurityUtils;
import me.batizhao.ims.controller.AdminMenuController;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(AdminMenuController.class)
public class MenuControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private MenuService menuService;

    private List<Menu> menuList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        menuList = new ArrayList<>();
        menuList.add(new Menu(1, 0).setName("工作台").setPermission("user_dashboard").setSort(1).setType(MenuTypeEnum.MENU.getType()));
        menuList.add(new Menu(2, 1).setName("权限管理").setPermission("ims_root").setSort(1).setType(MenuTypeEnum.MENU.getType()));
        menuList.add(new Menu(3, 2).setName("用户管理").setPermission("ims_user_admin").setSort(2).setType(MenuTypeEnum.MENU.getType()));
        menuList.add(new Menu(4, 2).setName("角色管理").setPermission("ims_role_admin").setSort(1).setType(MenuTypeEnum.MENU.getType()));
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindMenuTree4Me_thenSuccess() throws Exception {
        PecadoUser pecadoUser = new PecadoUser(1L, "zhangsan", "N_A", "张三", new ArrayList<>(), Collections.singletonList("1"), new HashSet<>(Collections.singletonList("admin")));

        List<Menu> trees = new ArrayList<>();
        Menu menu = menuList.get(0);

        List<TreeNode> children = new ArrayList<>();
        children.add(menuList.get(1));
        menu.setChildren(children);
        trees.add(menu);

        try (MockedStatic<SecurityUtils> mockStatic = mockStatic(SecurityUtils.class)) {
            mockStatic.when(SecurityUtils::getUser).thenReturn(pecadoUser);
            SecurityUtils.getUser();
//            mockStatic.verify(times(1), SecurityUtils::getUser);

            when(menuService.findMenuTreeByUserId(anyLong(), anyString())).thenReturn(trees);

            mvc.perform(get("/ims/menu/me"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].children[0].name", equalTo("权限管理")));
        }
    }

    @Test
    @WithMockUser
    public void givenRoleId_whenFindMenus_thenSuccess() throws Exception {
        doReturn(menuList).when(menuService).findMenusByRoleId(anyLong());

        mvc.perform(get("/ims/menu").param("roleId", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(4)))
                .andExpect(jsonPath("$.data[0].name", equalTo("工作台")));
    }

    @Test
    @WithMockUser
    void givenNothing_whenFindMenuTree_thenSuccess() throws Exception {
        List<Menu> menuTrees = new ArrayList<>();

        Menu menuTree = new Menu();
        menuTree.setName(menuList.get(0).getName());
        menuTree.setPermission(menuList.get(0).getPermission());
        menuTree.setPid(menuList.get(0).getPid());
        menuTree.setId(menuList.get(0).getId());

        Menu menuTree2 = new Menu();
        menuTree2.setName(menuList.get(1).getName());
        menuTree2.setPermission(menuList.get(1).getPermission());
        menuTree2.setPid(menuList.get(1).getPid());
        menuTree2.setId(menuList.get(1).getId());

        List<TreeNode> children = new ArrayList<>();
        children.add(menuTree2);
        menuTree.setChildren(children);
        menuTrees.add(menuTree);

        doReturn(menuTrees).when(menuService).findMenuTree(any(Menu.class), anyString());

        mvc.perform(get("/ims/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].children[0].name", equalTo("权限管理")));
    }

    @Test
    @WithMockUser
    public void givenId_whenFindMenu_thenSuccess() throws Exception {
        doReturn(menuList.get(0)).when(menuService).findMenuById(anyInt());

        mvc.perform(get("/ims/menu/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("工作台"));
    }

    @Test
    @WithMockUser
    public void givenMenu_whenSaveMenu_thenSuccess() throws Exception {
        Menu menu = menuList.get(0);
        menu.setId(null);

        doReturn(menuList.get(1)).when(menuService).saveOrUpdateMenu(any(Menu.class));

        mvc.perform(post("/ims/menu").with(csrf())
                .content(objectMapper.writeValueAsString(menu))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("权限管理"));

        verify(menuService).saveOrUpdateMenu(any(Menu.class));
    }

    @Test
    @WithMockUser
    public void givenMenu_whenUpdateMenu_thenSuccess() throws Exception {
        doReturn(menuList.get(0)).when(menuService).saveOrUpdateMenu(any(Menu.class));

        mvc.perform(post("/ims/menu").with(csrf())
                .content(objectMapper.writeValueAsString(menuList.get(0)))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.name").value("工作台"));

        verify(menuService).saveOrUpdateMenu(any(Menu.class));
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteMenu_thenSuccess() throws Exception {
        when(menuService.deleteById(anyInt())).thenReturn(true);

        mvc.perform(delete("/ims/menu").param("id", "1").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));

        verify(menuService).deleteById(anyInt());
    }

    @Test
    @WithMockUser
    public void givenId_whenDeleteMenu_thenFail() throws Exception {
        when(menuService.deleteById(anyInt())).thenReturn(false);

        mvc.perform(delete("/ims/menu").param("id", "1").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
                .andExpect(jsonPath("$.data").value("There are sub menus that cannot be deleted!"));

        verify(menuService).deleteById(anyInt());
    }

    @Test
    @WithMockUser
    public void givenMenu_whenUpdateStatus_thenSuccess() throws Exception {
        Menu requestBody = new Menu();

        when(menuService.updateStatus(any(Menu.class))).thenReturn(true);

        mvc.perform(post("/ims/menu/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(menuService).updateStatus(any(Menu.class));
    }
}