package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.mapper.UserRoleMapper;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.impl.UserRoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;

/**
 * @author batizhao
 * @since 2020-09-14
 */
@Slf4j
public class UserRoleServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserRoleService userRoleService() {
            return new UserRoleServiceImpl();
        }
    }

    @Autowired
    private UserRoleService userRoleService;

    @MockBean
    private UserRoleMapper userRoleMapper;

    @SpyBean
    private IService service;

    private List<UserRole> userRoleList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        userRoleList = new ArrayList<>();
        userRoleList.add(new UserRole().setUserId(1L).setRoleId(1L));
        userRoleList.add(new UserRole().setUserId(1L).setRoleId(2L));
    }

    @Test
    public void givenUserRole_whenUpdate_thenSuccess() {
        doReturn(true).when(service).remove(any(Wrapper.class));
        doReturn(true).when(service).saveBatch(anyList());

        Boolean b = userRoleService.updateUserRoles(userRoleList);
        assertThat(b, equalTo(true));

        doReturn(false).when(service).saveBatch(anyList());

        b = userRoleService.updateUserRoles(userRoleList);
        assertThat(b, equalTo(false));
    }
}