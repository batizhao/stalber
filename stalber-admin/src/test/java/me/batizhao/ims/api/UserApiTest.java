package me.batizhao.ims.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.BaseApiTest;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.common.core.constant.SecurityConstants;
import me.batizhao.ims.domain.User;
import me.batizhao.system.aspect.SystemLogAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MvcResult;
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
@Slf4j
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
        String accessToken = "Bearer eyJhbGciOiJSUzI1NiJ9.eyJ1c2VyX2lkIjoxLCJyb2xlX2lkIjpbMSwyXSwiZXhwIjoxNjI5NDMwMjA5LCJkZXB0X2lkIjpbMV0sImlhdCI6MTYyOTQyNjYwOSwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiIsImRwOmNvZGU6YWRtaW4iLCJkcDpjb2RlOmRlbGV0ZSIsImRwOmNvZGU6ZWRpdCIsImRwOmNvZGU6Z2VuIiwiZHA6Y29kZTppbXBvcnQiLCJkcDpjb2RlOnByZXZpZXciLCJkcDpjb2RlOnN5bmMiLCJkcDpkczphZGQiLCJkcDpkczphZG1pbiIsImRwOmRzOmRlbGV0ZSIsImRwOmRzOmVkaXQiLCJkcDpmb3JtOmFkZCIsImRwOmZvcm06YWRtaW4iLCJkcDpmb3JtOmRlbGV0ZSIsImRwOmZvcm06ZWRpdCIsImltczpkZXBhcnRtZW50OmFkZCIsImltczpkZXBhcnRtZW50OmFkbWluIiwiaW1zOmRlcGFydG1lbnQ6ZGVsZXRlIiwiaW1zOmRlcGFydG1lbnQ6ZWRpdCIsImltczptZW51OmFkZCIsImltczptZW51OmFkbWluIiwiaW1zOm1lbnU6ZGVsZXRlIiwiaW1zOm1lbnU6ZWRpdCIsImltczpwb3N0OmFkZCIsImltczpwb3N0OmFkbWluIiwiaW1zOnBvc3Q6ZGVsZXRlIiwiaW1zOnBvc3Q6ZWRpdCIsImltczpyb2xlOmFkZCIsImltczpyb2xlOmFkbWluIiwiaW1zOnJvbGU6ZGVsZXRlIiwiaW1zOnJvbGU6ZWRpdCIsImltczp1c2VyOmFkZCIsImltczp1c2VyOmFkbWluIiwiaW1zOnVzZXI6ZGVsZXRlIiwiaW1zOnVzZXI6ZWRpdCIsImltczp1c2VyOmV4cG9ydCIsImltczp1c2VyOmltcG9ydCIsIm9hOmNvbW1lbnQ6YWRkIiwib2E6Y29tbWVudDphZG1pbiIsIm9hOnRhc2s6YWRtaW4iLCJzeXN0ZW06ZGljdDphZGQiLCJzeXN0ZW06ZGljdDphZG1pbiIsInN5c3RlbTpkaWN0OmRlbGV0ZSIsInN5c3RlbTpkaWN0OmVkaXQiLCJzeXN0ZW06am9iOmFkZCIsInN5c3RlbTpqb2I6YWRtaW4iLCJzeXN0ZW06am9iOmRlbGV0ZSIsInN5c3RlbTpqb2I6ZWRpdCIsInN5c3RlbTpsb2c6YWRtaW4iLCJzeXN0ZW06bG9nOmNsZWFuIiwic3lzdGVtOmxvZzpkZWxldGUiLCJzeXN0ZW06bG9nOmV4cG9ydCJdLCJ1c2VybmFtZSI6ImFkbWluIn0.hEqDwa3NvDwI88ugkEhBX_RTM5JgN2DHQLiPMbOGC1fzVonGY_skAb8ZV-W12cTv3ADMRYLdc2SQP5ClGaHnuCxDjL6NJ5AMwx1ANmLsTNNB8xbmYyPEaMaBj0xPEOs5qp3c0ZoduEPwL0M8xNALCUFlbV0gNu5qWF1RMVvUAoYHFff3_bR7k3GjsIcbGzWELMMhwnTN08ipmBC7_rymZytGdIYIIr0Znjd9b6uiaVOfmk_-maR1wmPENjpSKgV_zJd7RQnX02CIUlsF1jy_trio41tnesF0KeR4gwqQLav5v-KontHBmZbmIuD46pdypLxtwpxPggSF7PAw_3sL1g";
        mvc.perform(get("/ims/users")
                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void givenInvalidToken_whenGetSecureRequest_thenUnauthorized() throws Exception {
        String accessToken = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
        mvc.perform(get("/ims/user")
                .header("Authorization", accessToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());
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
                .setPassword("123456").setUuid("xxxx");

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
        mvc.perform(delete("/ims/user").param("ids", "2,3")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    @Transactional
    public void givenId_whenDeleteUser_thenIsAdminError() throws Exception {
        mvc.perform(delete("/ims/user").param("ids", "1")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.UNKNOWN_ERROR.getCode()))
                .andExpect(jsonPath("$.data", containsString("Operation not allowed!")));
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
        MvcResult result = mvc.perform(post("/uaa/token")
                        .content("{\"username\":\"tom\",\"password\":\"123456\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", containsString("eyJhbGciOiJSUzI1NiJ9")))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String userAccessToken = SecurityConstants.TOKEN_PREFIX + JsonPath.parse(response).read("$.data");

        log.info("*** userAccessToken *** : {}", userAccessToken);

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
