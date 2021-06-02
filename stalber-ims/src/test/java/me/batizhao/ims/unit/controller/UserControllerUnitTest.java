package me.batizhao.ims.unit.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.common.domain.PecadoUser;
import me.batizhao.common.util.SecurityUtils;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.User;
import me.batizhao.ims.domain.UserInfoVO;
import me.batizhao.ims.domain.UserRole;
import me.batizhao.ims.controller.UserController;
import me.batizhao.ims.service.UserDepartmentService;
import me.batizhao.ims.service.UserPostService;
import me.batizhao.ims.service.UserRoleService;
import me.batizhao.ims.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
 * 注意，在 Spring Security 启用的情况下：
 * 1. post、put、delete 方法要加上 with(csrf())，否则会返回 403，因为这里控制了 config 扫描范围 csrf.disable 并没有生效
 * 2. 单元测试要控制扫描范围，防止 Spring Security Config 自动初始化，尤其是 UserDetailsService 自定义的情况（会加载 Mapper）
 * 3. 测试方法要加上 @WithMockUser，否则会返回 401
 *
 * @author batizhao
 * @since 2020-02-10
 */
@WebMvcTest(UserController.class)
public class UserControllerUnitTest extends BaseControllerUnitTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mvc;

    /**
     * 所有实现的接口都要 Mock
     */
    @MockBean
    private UserService userService;
    @MockBean
    private UserRoleService userRoleService;
    @MockBean
    private UserPostService userPostService;
    @MockBean
    private UserDepartmentService userDepartmentService;

    private List<User> userList;
    private IPage<User> userPageList;

    /**
     * Prepare test data.
     */
    @BeforeEach
    public void setUp() {
        userList = new ArrayList<>();
        userList.add(new User().setId(1L).setEmail("zhangsan@gmail.com").setUsername("zhangsan").setName("张三"));
        userList.add(new User().setId(2L).setEmail("lisi@gmail.com").setUsername("lisi").setName("李四"));
        userList.add(new User().setId(3L).setEmail("wangwu@gmail.com").setUsername("wangwu").setName("王五"));

        userPageList = new Page<>();
        userPageList.setRecords(userList);
    }

    @Test
    @WithMockUser
    public void givenUserName_whenFindUser_thenUserJson() throws Exception {
        String username = "zhangsan";

        List<Role> roleList = new ArrayList<>();
        roleList.add(new Role().setId(1L).setName("admin"));
        roleList.add(new Role().setId(2L).setName("common"));

        User user = userList.get(0);
        when(userService.findByUsername(username)).thenReturn(user);

        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUser(user);
        userInfoVO.setRoles(roleList.stream().map(Role::getName).collect(Collectors.toList()));
        when(userService.getUserInfo(user.getId())).thenReturn(userInfoVO);

        mvc.perform(get("/ims/user").param("username", username))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.user.email").value("zhangsan@gmail.com"))
                .andExpect(jsonPath("$.data.roles", hasSize(2)));

        verify(userService).findByUsername(anyString());
    }

//    @Test
//    @WithMockUser
//    public void givenName_whenFindUser_thenUserListJson() throws Exception {
//        String name = "张三";
//
//        //对数据集进行条件过滤
//        doAnswer(invocation -> {
//            Object arg0 = invocation.getArgument(0);
//
//            userList = userList.stream()
//                    .filter(p -> p.getName().equals(arg0)).collect(Collectors.toList());
//
//            return userList;
//        }).when(userService).findByName(name);
//
//        mvc.perform(get("/user").param("name", name))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data", hasSize(1)))
//                .andExpect(jsonPath("$.data[0].username", equalTo("zhangsan")));
//
//        verify(userService).findByName(name);
//    }

    @Test
    @WithMockUser
    public void givenId_whenFindUser_thenUserJson() throws Exception {
        Long id = 1L;

        when(userService.findById(id)).thenReturn(userList.get(0));

        mvc.perform(get("/ims/user/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("zhangsan@gmail.com"));

        verify(userService).findById(anyLong());
    }

    @Test
    @WithMockUser
    public void givenNothing_whenFindAllUser_thenUserListJson() throws Exception {
        when(userService.findUsers(any(Page.class), any(User.class), any())).thenReturn(userPageList);

        mvc.perform(get("/ims/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(content().string(stringContainsInOrder("zhangsan", "lisi", "wangwu")))
                .andExpect(jsonPath("$.data.records", hasSize(3)))
                .andExpect(jsonPath("$.data.records[0].username", equalTo("zhangsan")));

        verify(userService).findUsers(any(Page.class), any(User.class), any());
    }

    @Test
    @WithMockUser
    public void givenJson_whenSaveUser_thenSuccessJson() throws Exception {
        User requestBody = new User().setEmail("zhaoliu@gmail.com").setUsername("zhaoliu").setPassword("xxx").setName("xxx");

        when(userService.saveOrUpdateUser(any(User.class)))
                .thenReturn(userList.get(0));

        mvc.perform(post("/ims/user").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(1)));

        verify(userService).saveOrUpdateUser(any(User.class));
    }

    @Test
    @WithMockUser
    public void givenJson_whenUpdateUser_thenSuccessJson() throws Exception {
        User requestBody = new User().setId(2L).setEmail("zhaoliu@gmail.com").setUsername("zhaoliu").setPassword("xxx").setName("xxx");

        when(userService.saveOrUpdateUser(any(User.class)))
                .thenReturn(userList.get(1));

        mvc.perform(post("/ims/user").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", equalTo(2)))
                .andExpect(jsonPath("$.data.username", equalTo("lisi")));

        verify(userService).saveOrUpdateUser(any(User.class));
    }

    /**
     * 删除成功的情况
     *
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void givenId_whenDeleteUser_thenSuccess() throws Exception {
        when(userService.deleteByIds(anyList())).thenReturn(true);

        mvc.perform(delete("/ims/user").param("ids", "1,2").with(csrf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).deleteByIds(anyList());
    }

    /**
     * 更新用户状态
     *
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void givenUser_whenUpdateStatus_thenSuccess() throws Exception {
        User requestBody = new User().setId(2L).setStatus("close");

        when(userService.updateStatus(any(User.class))).thenReturn(true);

        mvc.perform(post("/ims/user/status").with(csrf())
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));

        verify(userService).updateStatus(any(User.class));
    }

    /**
     * Mock Static Method
     * @throws Exception
     */
    @Test
    @WithMockUser
    public void givenNothing_whenGetUserInfo_thenSuccess() throws Exception {
        PecadoUser pecadoUser = new PecadoUser(1L, Collections.singletonList(2), Collections.singletonList(1L), "zhangsan", "N_A", true, true, true, true, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

        try (MockedStatic<SecurityUtils> mockStatic = mockStatic(SecurityUtils.class)) {
            mockStatic.when(SecurityUtils::getUser).thenReturn(pecadoUser);
            SecurityUtils.getUser();
            mockStatic.verify(times(1), SecurityUtils::getUser);

            UserInfoVO userInfoVO = new UserInfoVO();
            userInfoVO.setUser(userList.get(0));

            doReturn(userInfoVO).when(userService).getUserInfo(1L);

            mvc.perform(get("/ims/user/me"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                    .andExpect(jsonPath("$.data.user.username").value("zhangsan"));
        }
    }

    @Test
    @WithMockUser
    public void giveUserRoles_whenAdd_thenSuccess() throws Exception {
        List<UserRole> userRoleList = new ArrayList<>();
        userRoleList.add(new UserRole().setUserId(1L).setRoleId(1L));
        userRoleList.add(new UserRole().setUserId(1L).setRoleId(2L));

        doReturn(true).when(userRoleService).updateUserRoles(any(List.class));

        mvc.perform(post("/ims/user/role").with(csrf())
                .content(objectMapper.writeValueAsString(userRoleList))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }
}