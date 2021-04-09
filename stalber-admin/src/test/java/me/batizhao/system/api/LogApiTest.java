package me.batizhao.system.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.admin.security.SecurityConstants;
import me.batizhao.common.constant.ResultEnum;
import me.batizhao.system.domain.Log;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author batizhao
 * @since 2020-03-18
 **/
@DirtiesContext
public class LogApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    public void givenLogDTO_whenPostLog_thenSuccess() throws Exception {
        Log logDTO = new Log().setDescription("根据用户ID查询角色").setSpend(20).setClassMethod("findRolesByUserId")
                .setClassName("me.batizhao.ims.web.RoleController").setClientId("client_app").setHttpRequestMethod("POST")
                .setIp("127.0.0.1").setCreateTime(LocalDateTime.now()).setUrl("http://localhost:5000/role").setUsername("test");

        mvc.perform(post("/system/log")
                .header("Authorization", adminAccessToken)
                .content(objectMapper.writeValueAsString(logDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data", equalTo(true)));
    }

}
