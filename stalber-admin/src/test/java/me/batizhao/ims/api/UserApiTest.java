package me.batizhao.ims.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.admin.security.SecurityConstants;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.ims.domain.User;
import me.batizhao.system.aspect.SystemLogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * <p>
 * 1. 在 OAuth 开启的情况下，不再需要 @WithMockUser 来模拟用户
 * 2. 这里要注意一个问题，当使用 Spring Security regexMatchers 时，例如 /user?username=admin 这种情况
 * get("/user").param("username", "xx") 会存在匹配不到的情况，要使用 get("/user?username=xx") 来代替
 * 具体可以看这个 Issues：https://github.com/spring-projects/spring-framework/issues/20040
 * </p>
 *
 * @author batizhao
 * @since 2020-02-11
 */
@DirtiesContext
public class UserApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @SpyBean
    private SystemLogAspect systemLogAspect;

    @Test
    public void givenUserName_whenFindUser_thenSuccess() throws Exception {
        mvc.perform(get("/ims/user?username=admin")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.user.email").value("admin@qq.com"))
                .andExpect(jsonPath("$.data.roles", hasSize(2)));
    }

    /**
     * Param 或者 PathVariable 校验失败的情况
     *
     * @throws Exception
     */
    @Test
    public void givenInvalidUserName_whenFindUser_thenValidateFailed() throws Exception {
        mvc.perform(get("/ims/user?username=xx")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("个数必须在")));
    }

    @Test
    public void givenUserName_whenFindUser_theNotFound() throws Exception {
        mvc.perform(get("/ims/user?username=xxxx")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.RESOURCE_NOT_FOUND.getCode()));
    }

    /**
     * 没有传递 @Inner header
     *
     * @throws Exception
     */
    @Test
    public void givenUserNameButNoInnerHeader_whenFindUser_thenNull() throws Exception {
        mvc.perform(get("/ims/user?username=bob"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Full authentication is required")));
    }

    @Test
    public void givenId_whenFindUser_thenUserJson() throws Exception {
        mvc.perform(get("/ims/user/{id}", 1L)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.email").value("admin@qq.com"));
    }

    @Test
    public void givenNoToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        mvc.perform(get("/ims/user/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Full authentication is required")));
    }

    @Test
    public void givenNothing_whenFindAllUser_thenUserListJson() throws Exception {
        mvc.perform(get("/ims/users")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.records", hasSize(6)))
                .andExpect(jsonPath("$.data.records[0].username", equalTo("admin")));
    }

    @Test
    public void givenExpiredToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjowLCJ1c2VyX2lkIjoxLCJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE1OTY3ODU2NDAsImRlcHRfaWQiOjEsIm1lc3NhZ2UiOiJvayIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiNTJkYTA2YzktMWRiMi00NzdmLWJkZjItY2VhOTY1ZTJjNTM1IiwiY2xpZW50X2lkIjoiY2xpZW50X2FwcCIsInVzZXJuYW1lIjoiYWRtaW4ifQ.wVqtJm7__YO8pnh79JMKt1YO5GuIryDj7mCqkLPMSvA";
        mvc.perform(get("/ims/users")
                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Access token expired")));
    }

    @Test
    public void givenInvalidToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        mvc.perform(get("/ims/user")
                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.OAUTH2_TOKEN_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Cannot convert access token")));
    }

    /**
     * 如果需要回滚数据，这里需要加上 @Transactional 注解
     *
     * @throws Exception
     */
    @Test
    @Transactional
    public void givenJson_whenSaveUser_thenSucceedJson() throws Exception {
        User requestBody = new User()
                .setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/ims/user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", notNullValue()))
                .andExpect(jsonPath("$.data.username", equalTo("daxia")));

        verify(systemLogAspect).around(any(ProceedingJoinPoint.class), any(SystemLog.class));
    }

    /**
     * RequestBody 校验失败的情况1
     *
     * @throws Exception
     */
    @Test
    public void givenJson_whenSaveUser_thenValidateFailed() throws Exception {
        User requestBody = new User().setName("daxia").setEmail("daxia@gmail.com");

        mvc.perform(post("/ims/user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data[0]", containsString("is not blank")));
    }

    /**
     * RequestBody 校验失败的情况2
     *
     * @throws Exception
     */
    @Test
    public void givenNoJson_whenSaveUser_thenValidateFailed() throws Exception {
        mvc.perform(post("/ims/user")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Required request body is missing")));
    }

    @Test
    @Transactional
    public void givenJson_whenUpdateUser_thenSucceedJson() throws Exception {
        User requestBody = new User()
                .setId(8L).setName("daxia").setEmail("daxia@gmail.com").setUsername("daxia")
                .setPassword("123456");

        mvc.perform(post("/ims/user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.username", equalTo("daxia")));
    }

    @Test
    @Transactional
    public void givenId_whenDeleteUser_thenSucceed() throws Exception {
        mvc.perform(delete("/ims/user").param("ids", "1,2")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

    /**
     * 测试参数检验失败的情况
     *
     * @throws Exception
     */
    @Test
    public void givenStringId_whenDeleteUser_thenValidateFail() throws Exception {
        mvc.perform(delete("/ims/user").param("ids", "xxxx")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Failed to convert value of type")));
    }

    @Test
    public void givenInvalidRole_whenGetSecureRequest_thenForbidden() throws Exception {
        mvc.perform(delete("/ims/user").param("ids", "1,2")
                .header("Authorization", userAccessToken))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PERMISSION_FORBIDDEN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("不允许访问")));
    }

    @Test
    public void givenId_whenPutUser_thenMethodNotSupported() throws Exception {
        mvc.perform(put("/ims/user/{id}", 3L)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.PARAMETER_INVALID.getCode()))
                .andExpect(jsonPath("$.data", containsString("Request method 'PUT' not supported")));
    }

    @Test
    public void givenAuthentication_whenGetCurrentUser_thenMe() throws Exception {
        mvc.perform(get("/ims/user/me")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.user.username").value("admin"));
    }
}
