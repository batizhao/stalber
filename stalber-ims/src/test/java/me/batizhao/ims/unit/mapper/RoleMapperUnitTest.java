package me.batizhao.ims.unit.mapper;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.mapper.RoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Slf4j
public class RoleMapperUnitTest extends BaseMapperUnitTest {

    @Autowired
    private RoleMapper roleMapper;

    @Test
    public void testFindRolesByUserId() {
        List<Role> roles = roleMapper.findRolesByUserId(1L);

        assertThat(roles, hasItem(allOf(hasProperty("id", is(1L)),
                hasProperty("name", is("普通用户")))));
    }

//    @Test
//    public void testFindRoleMenus() {
//        List<RoleMenu> roleMenus = roleMapper.findRoleMenus();
//
//        log.info("rolePermissions: {}", roleMenus);
//
//        assertThat(roleMenus, hasItem(allOf(hasProperty("roleCode", is("ROLE_USER")),
//                hasProperty("path", is("/dashboard")))));
//
//        assertThat(roleMenus, hasItem(allOf(hasProperty("roleCode", is("ROLE_ADMIN")),
//                hasProperty("path", is("/ims")))));
//    }

}
