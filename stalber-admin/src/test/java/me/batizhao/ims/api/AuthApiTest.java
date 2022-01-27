package me.batizhao.ims.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.ims.domain.LoginDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * OAuth 安全框架集成测试
 * 这些测试只有 @EnableAuthorizationServer 的情况下有意义
 * 这个类不需要预先获取 access_token，所以不要继承基类
 *
 * @author batizhao
 * @since 2020-03-02
 **/
@DirtiesContext
public class AuthApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 获取 adminAccessToken
     *
     * 如果有增加新的菜单权限，需要使用这个方法重新获取测试 token，具体步骤
     * 1、修改当前激活的 profile 下的 pecado.jwt.expire 为 10000，意思为 10000 分钟过期，做为测试 token
     * 2、在输出中找到 token，更新到 application.yml pecado.token.admin 中
     * 3、恢复 pecado.jwt.expire，保证正常的过期条件
     * 4、每次有新的菜单增加，都需要重做这些步骤，否则测试用例会抛出 403 异常
     *
     * @throws Exception
     */
    @Test
    public void givenAdmin_whenGetAccessToken_thenSuccess() throws Exception {
        LoginDTO loginDTO = new LoginDTO().setUsername("admin").setPassword("123456");
        mvc.perform(post("/uaa/token")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    /**
     * 获取 userAccessToken
     *
     * 如果有增加新的菜单权限，需要使用这个方法重新获取测试 token，具体步骤
     * 1、修改当前激活的 profile 下的 pecado.jwt.expire 为 10000，意思为 10000 分钟过期，做为测试 token
     * 2、在输出中找到 token，更新到 application.yml pecado.token.user 中
     * 3、恢复 pecado.jwt.expire，保证正常的过期条件
     * 4、每次有新的菜单增加，都需要重做这些步骤，否则测试用例会抛出 403 异常
     *
     * @throws Exception
     */
    @Test
    public void givenUser_whenGetAccessToken_thenSuccess() throws Exception {
        LoginDTO loginDTO = new LoginDTO().setUsername("tom").setPassword("123456");
        mvc.perform(post("/uaa/token")
                        .content(objectMapper.writeValueAsString(loginDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    public void givenNoPassword_whenGetAccessToken_thenError() throws Exception {
        LoginDTO loginDTO = new LoginDTO().setUsername("admin");
        mvc.perform(post("/uaa/token")
                .content(objectMapper.writeValueAsString(loginDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("password is not blank")));
    }

    @Test
    public void givenInValidPassword_whenGetAccessToken_thenError() throws Exception {
        LoginDTO loginDTO = new LoginDTO().setUsername("admin").setPassword("12345678");
        mvc.perform(post("/uaa/token")
                .content(objectMapper.writeValueAsString(loginDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("用户名或密码错误")));
    }
}
