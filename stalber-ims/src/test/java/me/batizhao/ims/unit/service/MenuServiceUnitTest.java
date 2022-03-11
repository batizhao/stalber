package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.constant.MenuScopeEnum;
import me.batizhao.common.core.constant.MenuTypeEnum;
import me.batizhao.common.core.domain.TreeNode;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.MenuMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.impl.MenuServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class MenuServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public MenuService menuService() {
            return new MenuServiceImpl();
        }
    }

    @MockBean
    private MenuMapper menuMapper;
    @MockBean
    private RoleService roleService;
    @MockBean
    private RoleMenuService roleMenuService;

    @Autowired
    private MenuService menuService;

    @MockBean
    private ServiceImpl service;

    private List<Menu> menuList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        menuList = new ArrayList<>();
        menuList.add(new Menu(1, 0).setName("工作台").setPermission("user_dashboard").setSort(1).setType(MenuTypeEnum.MENU.getType()).setScope(MenuScopeEnum.ADMIN.getType()));
        menuList.add(new Menu(2, 1).setName("权限管理").setPermission("ims_root").setSort(1).setType(MenuTypeEnum.MENU.getType()).setScope(MenuScopeEnum.ADMIN.getType()));
        menuList.add(new Menu(3, 2).setName("用户管理").setPermission("ims_user_admin").setSort(2).setType(MenuTypeEnum.MENU.getType()).setScope(MenuScopeEnum.ADMIN.getType()));
        menuList.add(new Menu(4, 2).setName("角色管理").setPermission("ims_role_admin").setSort(1).setType(MenuTypeEnum.MENU.getType()).setScope(MenuScopeEnum.ADMIN.getType()));
    }

    @Test
    public void givenRoleId_whenFindMenus_thenSuccess() {
        when(menuMapper.findMenusByRoleId(anyLong()))
                .thenReturn(menuList);

        List<Menu> menus = menuService.findMenusByRoleId(1L);

        log.info("roles: {}", menus);

        assertThat(menus, hasSize(4));
        assertThat(menus, hasItems(hasProperty("name", is("工作台"))));
        assertThat(menus.get(0).getMeta(), hasProperty("title", is("工作台")));
    }

    @Test
    public void givenUserId_whenFindMenus_thenSuccess() {
        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setName("admin"));

        when(roleService.findRolesByUserId(anyLong())).thenReturn(roleList);
        when(menuMapper.findMenusByRoleId(anyLong())).thenReturn(menuList);

        List<Menu> menus = menuService.findMenuTreeByUserId(1L, MenuScopeEnum.ADMIN.getType());

        log.info("menus: {}", menus);

        assertThat(menus, hasSize(1));
        assertThat(menus, hasItems(hasProperty("name", is("工作台")),
                hasProperty("children", hasSize(1))));
    }

    @Test
    public void givenNothing_whenFindMenuTree_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Menu.class);

        doReturn(menuList).when(menuMapper).selectList(any());

        List<Menu> menuTree = menuService.findMenuTree(null, MenuScopeEnum.ADMIN.getType());

        assertThat(menuTree, hasSize(1));
        assertThat(menuTree, hasItems(hasProperty("name", is("工作台")),
                hasProperty("children", hasSize(1))));

        List<TreeNode> treeNodes = menuTree.get(0).getChildren().get(0).getChildren();
        assertThat(treeNodes, hasSize(2));

        doReturn(menuList).when(menuMapper).selectList(any(Wrapper.class));
        menuTree = menuService.findMenuTree(menuList.get(0), MenuScopeEnum.ADMIN.getType());
        assertThat(menuTree, hasSize(1));

        doReturn(menuList).when(menuMapper).selectList(any(Wrapper.class));
        menuTree = menuService.findMenuTree(menuList.get(0).setName(""), MenuScopeEnum.ADMIN.getType());
        assertThat(menuTree, hasSize(1));
    }

    @Test
    public void givenNonParent_whenFilterMenus_thenSuccess() {
        List<Menu> menus = menuService.filterMenu((new HashSet<>(menuList)), null, MenuScopeEnum.ADMIN.getType());
        assertThat(menus, hasSize(1));
        assertThat(menus, hasItems(hasProperty("name", is("工作台")),
                hasProperty("children", hasSize(1))));

        List<TreeNode> treeNodes = menus.get(0).getChildren().get(0).getChildren();
        assertThat(treeNodes, hasSize(2));
        assertThat(treeNodes, containsInRelativeOrder(allOf(hasProperty("name", is("角色管理")),
                                                            hasProperty("permission", is("ims_role_admin"))),
                                                      allOf(hasProperty("name", is("用户管理")),
                                                            hasProperty("permission", is("ims_user_admin")))));
    }

    @Test
    public void givenParent_whenFilterMenus_thenSuccess() {
        List<Menu> menus = menuService.filterMenu((new HashSet<>(menuList)), 1, MenuScopeEnum.ADMIN.getType());
        assertThat(menus, hasSize(1));
        assertThat(menus, hasItems(hasProperty("name", is("权限管理")),
                hasProperty("children", hasSize(2))));

        List<TreeNode> treeNodes = menus.get(0).getChildren();
        assertThat(treeNodes, hasSize(2));
        assertThat(treeNodes, containsInRelativeOrder(allOf(hasProperty("name", is("角色管理")),
                hasProperty("permission", is("ims_role_admin"))),
                allOf(hasProperty("name", is("用户管理")),
                        hasProperty("permission", is("ims_user_admin")))));
    }

    @Test
    public void givenId_whenFindMenu_thenSuccess() {
        doReturn(menuList.get(0)).when(menuMapper).selectById(anyInt());

        Menu menu = menuService.findMenuById(1);

        assertThat(menu, hasProperty("name", equalTo("工作台")));
    }

    @Test
    public void givenId_whenFindMenu_thenNotFound() {
        when(menuMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> menuService.findMenuById(1));

        verify(menuMapper).selectById(any());
    }

    @Test
    public void givenMenu_whenSaveOrUpdate_thenSuccess() {
        Menu request_body = new Menu();

        // insert 不带 id
        doReturn(1).when(menuMapper).insert(any(Menu.class));

        Menu menu = menuService.saveOrUpdateMenu(request_body);
        log.info("menu: {}", menu);

        verify(menuMapper).insert(any());

        // update 需要带 id
        doReturn(1).when(menuMapper).updateById(any(Menu.class));

        menu = menuService.saveOrUpdateMenu(menuList.get(0));
        log.info("menu: {}", menu);

        verify(menuMapper).updateById(any());
    }

    @Test
    public void givenIds_whenDelete_thenSuccess() {
        doReturn(menuList).when(menuMapper).selectList(any(Wrapper.class));
        Boolean b = menuService.deleteById(1);
        assertThat(b, equalTo(false));

        doReturn(new ArrayList<Menu>()).when(menuMapper).selectList(any(Wrapper.class));
        doReturn(true).when(service).removeById(anyInt());
        doReturn(true).when(roleMenuService).remove(any(Wrapper.class));

        b = menuService.deleteById(1);
        assertThat(b, equalTo(true));
    }

    @Test
    public void givenMenu_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Menu.class);

        doReturn(1).when(menuMapper).update(any(), any(Wrapper.class));
        assertThat(menuService.updateStatus(menuList.get(0)), equalTo(true));

        doReturn(0).when(menuMapper).update(any(), any(Wrapper.class));
        assertThat(menuService.updateStatus(menuList.get(0)), equalTo(false));

        verify(menuMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}