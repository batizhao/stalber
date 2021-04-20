package me.batizhao.ims.unit.service;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.exception.NotFoundException;
import me.batizhao.common.exception.StalberException;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.domain.UserInfoVO;
import me.batizhao.ims.mapper.UserMapper;
import me.batizhao.ims.service.MenuService;
import me.batizhao.ims.service.RoleService;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.UserService;
import me.batizhao.ims.service.impl.UserServiceImpl;
import org.apache.ibatis.builder.MapperBuilderAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
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
public class UserServiceUnitTest extends BaseServiceUnitTest {

    /**
     * Spring Boot 提供了 @TestConfiguration 注释，可用于 src/test/java 中的类，以指示不应通过扫描获取它们。
     */
    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }

    @MockBean
    private UserMapper userMapper;
    @MockBean
    private RoleService roleService;
    @MockBean
    private MenuService menuService;
    @MockBean
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @SpyBean
    private ServiceImpl service;

    private List<User> userList;
    private Page<User> userPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        userList = new ArrayList<>();
        userList.add(new User().setId(1L).setEmail("zhangsan@gmail.com").setUsername("zhangsan").setName("张三").setPassword("123456"));
        userList.add(new User().setId(2L).setEmail("lisi@gmail.com").setUsername("lisi").setName("李四").setPassword("123456"));
        userList.add(new User().setId(3L).setEmail("wangwu@gmail.com").setUsername("wangwu").setName("王五").setPassword("123456"));

        userPageList = new Page<>();
        userPageList.setRecords(userList);
    }

    @Test
    public void givenUserName_whenFindUser_thenUser() {
        String username = "zhangsan";

        when(userMapper.selectOne(any()))
                .thenReturn(userList.get(0));

        User user = userService.findByUsername(username);

        assertThat(user.getUsername(), equalTo(username));
        assertThat(user.getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void givenUserName_whenFindUser_thenNull() {
        String username = "zhangsan";

        when(userMapper.selectOne(any()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> userService.findByUsername(username));

        verify(userMapper).selectOne(any());
    }

//    @Test
//    public void givenName_whenFindUser_thenUserList() {
//        String name = "张三";
//
//        when(userMapper.selectList(any())).thenReturn(userList.subList(0,1));
//
//        List<User> users = userService.findByName(name);
//
//        verify(userMapper).selectList(any());
//
//        log.info("users: {}", users);
//
//        assertThat(users, hasSize(1));
//        assertThat(users, hasItems(hasProperty("username", is("zhangsan"))));
//    }

//    @Test
//    public void givenName_whenFindUser_thenEmpty() {
//        userList.clear();
//        when(userMapper.selectList(any())).thenReturn(userList);
//
//        List<User> users = userService.findByName("xxxx");
//
//        verify(userMapper).selectList(any());
//
//        log.info("users: {}", users);
//
//        assertThat(users, hasSize(0));
//    }

    @Test
    public void givenNothing_whenFindAllUser_thenSuccess() {
        when(userMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(userPageList);

        IPage<User> users = userService.findUsers(new Page<>(), new User().setUsername("tom"));

        assertThat(users.getRecords(), iterableWithSize(3));
        assertThat(users.getRecords(), hasItems(hasProperty("username", is("zhangsan")),
                                      hasProperty("email", is("lisi@gmail.com")),
                                      hasProperty("email", is("wangwu@gmail.com"))));

        assertThat(users.getRecords(), containsInAnyOrder(allOf(hasProperty("email", is("zhangsan@gmail.com")),
                                                      hasProperty("username", is("zhangsan"))),
                                                allOf(hasProperty("email", is("lisi@gmail.com")),
                                                      hasProperty("username", is("lisi"))),
                                                allOf(hasProperty("email", is("wangwu@gmail.com")),
                                                      hasProperty("username", is("wangwu")))));

        when(userMapper.selectPage(any(Page.class), any(Wrapper.class)))
                .thenReturn(userPageList);

        users = userService.findUsers(new Page<>(), new User().setName("tom"));

        assertThat(users.getRecords(), iterableWithSize(3));
    }

    @Test
    public void givenUserId_whenFindUser_thenUser() {
        when(userMapper.selectById(1L))
                .thenReturn(userList.get(0));

        User user = userService.findById(1L);

        assertThat(user.getUsername(), equalTo("zhangsan"));
        assertThat(user.getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void givenUserId_whenFindUser_thenNull() {
        when(userMapper.selectById(anyLong()))
                .thenReturn(null);

        assertThrows(NotFoundException.class, () -> {
            userService.findById(1L);
        });

        verify(userMapper).selectById(anyLong());
    }

    @Test
    public void givenUserJson_whenSaveOrUpdateUser_thenSuccess() {
        User user_test_data = new User().setEmail("zhaoliu@gmail.com").setUsername("zhaoliu").setPassword("xxx").setName("xxx");

        BCryptPasswordEncoder bcryptPasswordEncoder = new BCryptPasswordEncoder();
        String hashPass = bcryptPasswordEncoder.encode(user_test_data.getPassword());
        log.info("hashPass: {}", hashPass);

        boolean bool = bcryptPasswordEncoder.matches(user_test_data.getPassword(), hashPass);
        assertThat(bool, equalTo(true));

        user_test_data.setPassword(hashPass);
        user_test_data.setCreateTime(LocalDateTime.now());
        log.info("user_test_data: {}", user_test_data);

        //这里注意 saveOrUpdate 是第三方的方法，所以用了 spy 对 UserService 做了个 mock
        //并且这里只能使用 doReturn...when 的方式，不能使用 when...thenReturn
//        UserService userService = spy(new UserServiceIml());
//        doReturn(true).when(userService).saveOrUpdate(user_test_data);

        // insert 不带 id
        doReturn(1).when(userMapper).insert(any());

        User user = userService.saveOrUpdateUser(user_test_data);
        log.info("user: {}", user);

        verify(userMapper).insert(any());

        // update 需要带 id
        doReturn(1).when(userMapper).updateById(any());

        user = userService.saveOrUpdateUser(userList.get(0));
        log.info("user: {}", user);

        verify(userMapper).updateById(any());
    }

    @Test
    public void givenIds_whenDelete_thenSuccess() {
        doReturn(true).when(service).removeByIds(anyList());
        doReturn(true).when(userRoleService).remove(any(Wrapper.class));

        Boolean b = userService.deleteByIds(Arrays.asList(2L, 3L));
        assertThat(b, equalTo(true));
    }

    @Test
    public void givenIds_whenDelete_thenException() {
        doReturn(true).when(service).removeByIds(anyList());
        doReturn(true).when(userRoleService).remove(any(Wrapper.class));

        assertThrows(StalberException.class, () -> userService.deleteByIds(Arrays.asList(1L, 3L)));
    }

    @Test
    public void givenUsername_whenGetUserInfo_thenSuccess() {
        doReturn(userList.get(0)).when(userMapper).selectById(anyLong());

        UserInfoVO uiv = userService.getUserInfo(1L);

        assertThat(uiv.getUser().getEmail(), equalTo("zhangsan@gmail.com"));
    }

    @Test
    public void givenUsername_whenGetUserInfo_thenNotFound() {
        doReturn(null).when(userMapper).selectOne(any());

        assertThrows(NotFoundException.class, () -> userService.getUserInfo(1L));
    }

    @Test
    public void givenUser_whenUpdateStatus_thenSuccess() {
        //Fix can not find lambda cache for this entity
        TableInfoHelper.initTableInfo(new MapperBuilderAssistant(new MybatisConfiguration(), ""), User.class);

        doReturn(1).when(userMapper).update(any(), any(Wrapper.class));
        assertThat(userService.updateStatus(userList.get(0)), equalTo(true));

        doReturn(0).when(userMapper).update(any(), any(Wrapper.class));
        assertThat(userService.updateStatus(userList.get(0)), equalTo(false));

        verify(userMapper, times(2)).update(any(), any(LambdaUpdateWrapper.class));
    }
}