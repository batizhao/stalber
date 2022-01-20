package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.common.core.exception.StalberException;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.RoleMapper;
import me.batizhao.ims.service.RoleDepartmentService;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.impl.RoleServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

/**
 * @author batizhao
 * @since 2020-02-08
 */
@Slf4j
public class RoleServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public RoleService roleService() {
            return new RoleServiceImpl();
        }
    }

    @MockBean
    private RoleMapper roleMapper;
    @MockBean
    private UserRoleService userRoleService;
    @MockBean
    private RoleMenuService roleMenuService;
    @MockBean
    private RoleDepartmentService roleDepartmentService;

    @Autowired
    private RoleService roleService;

    @MockBean
    private ServiceImpl service;

    private List<Role> roleList;
    private Page<Role> rolePageList;

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
    public void givenNothing_whenFindAllRole_thenSuccess() {
        when(roleMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(rolePageList);

        IPage<Role> roles = roleService.findRoles(new Page<>(), new Role());

        assertThat(roles.getRecords(), iterableWithSize(3));
        assertThat(roles.getRecords(), hasItems(hasProperty("name", equalTo("zhangsan")),
                hasProperty("name", equalTo("lisi")),
                hasProperty("name", equalTo("wangwu"))));

        rolePageList.setRecords(roleList.subList(1, 2));
        when(roleMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(rolePageList);

        roles = roleService.findRoles(new Page<>(), new Role().setName("lname"));
        assertThat(roles.getRecords(), iterableWithSize(1));
        assertThat(roles.getRecords(), hasItems(hasProperty("name", equalTo("lisi"))));
    }

    @Test
    public void givenRoleId_whenFindRole_thenSuccess() {
        when(roleMapper.selectById(1L))
                .thenReturn(roleList.get(0));

        Role role = roleService.findById(1L);

        assertThat(role.getName(), equalTo("zhangsan"));
    }

    @Test
    public void givenRoleId_whenFindRole_thenNotFound() {
        when(roleMapper.selectById(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> roleService.findById(1L));

        verify(roleMapper).selectById(any());
    }

    @Test
    public void givenRoleJson_whenSaveOrUpdateRole_thenSuccess() {
        Role role_test_data = new Role().setName("zhaoliu");

        // insert 不带 id
        doReturn(1).when(roleMapper).insert(any(Role.class));

        roleService.saveOrUpdateRole(role_test_data);

        verify(roleMapper).insert(any(Role.class));

        // update 需要带 id
        doReturn(1).when(roleMapper).updateById(any(Role.class));

        roleService.saveOrUpdateRole(roleList.get(0));

        verify(roleMapper).updateById(any(Role.class));
    }

    @Test
    public void givenIds_whenDelete_thenSuccess() {
        doReturn(true).when(service).removeByIds(anyList());
        doReturn(true).when(userRoleService).remove(any(Wrapper.class));
        doReturn(true).when(roleMenuService).remove(any(Wrapper.class));
        doReturn(true).when(roleDepartmentService).remove(any(Wrapper.class));

        assertThrows(StalberException.class, () -> roleService.deleteByIds(Arrays.asList(1L, 2L)));

        Boolean b = roleService.deleteByIds(Arrays.asList(3L));
        assertThat(b, equalTo(true));
    }

    @Test
    public void givenRole_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), Role.class);

        doReturn(1).when(roleMapper).update(any(), any(Wrapper.class));
        assertThat(roleService.updateStatus(roleList.get(0)), equalTo(true));

        doReturn(0).when(roleMapper).update(any(), any(Wrapper.class));
        assertThat(roleService.updateStatus(roleList.get(0)), equalTo(false));

        verify(roleMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}