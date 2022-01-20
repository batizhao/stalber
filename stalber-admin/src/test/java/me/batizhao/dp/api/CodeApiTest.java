package me.batizhao.dp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.common.core.constant.ResultEnum;
import me.batizhao.dp.domain.Code;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
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
 * @since 2021-01-07
 */
public class CodeApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenId_whenFindCode_thenNotFound() throws Exception {
        mvc.perform(get("/dp/code/{id}", 10000L)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.RESOURCE_NOT_FOUND.getCode()));
    }

    @Test
    public void givenNothing_whenFindAllCode_thenSuccess() throws Exception {
        mvc.perform(get("/dp/codes")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    @Transactional
    public void givenJson_whenSaveCode_thenSuccess() throws Exception {
        Code requestBody = new Code()
                .setTableName("daxia").setTableComment("daxia@gmail.com").setDsName("ims").setEngine("InnoDB")
                .setClassName("Demo").setClassComment("xxx").setClassAuthor("batizhao").setPackageName("cn.sh")
                .setModuleName("system").setTemplate("xxxx").setMappingPath("path");

        mvc.perform(post("/dp/code")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data.id", notNullValue()));
    }

    @Test
    @Transactional
    public void givenJson_whenUpdateCode_thenSuccess() throws Exception {
        Code requestBody = new Code()
                .setId(1L).setTableName("daxia").setTableComment("daxia@gmail.com");

        mvc.perform(post("/dp/code")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    @Transactional
    public void givenId_whenDeleteCode_thenSuccess() throws Exception {
        mvc.perform(delete("/dp/code").param("ids", "1,2")
                .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
                .andExpect(jsonPath("$.data").value(true));
    }

//    @Test
//    public void givenConfig_whenGenerateCode_thenSuccess() throws Exception {
//        GenConfig requestBody = new GenConfig().setTableName("ds").setAuthor("batizhao").setComments("comment")
//                .setModuleName("system").setPackageName("me.batizhao");
//
//        mvc.perform(post("/code").with(csrf())
//                .header("Authorization", adminAccessToken)
//                .content(objectMapper.writeValueAsString(requestBody))
//                .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(header().string("Content-Disposition", "attachment; filename=ds.zip"));
//    }

}
