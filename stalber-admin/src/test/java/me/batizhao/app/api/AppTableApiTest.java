package me.batizhao.app.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.batizhao.BaseApiTest;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.domain.AppTableColumn;
import me.batizhao.common.core.constant.ResultEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class AppTableApiTest extends BaseApiTest {

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void givenId_whenFindAppTable_thenSuccess() throws Exception {
//        mvc.perform(get("/app/table/{id}", 1L)
//                        .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
//    }

    @Test
    public void givenNothing_whenFindAllAppTable_thenSuccess() throws Exception {
        mvc.perform(get("/app/tables")
                        .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    @Transactional
    public void givenJson_whenSaveAppTable_thenSuccess() throws Exception {
        AppTableColumn ac0 = new AppTableColumn().setName("id").setComment("主键").setType("bigint").setRequired(true).setPrimary(true).setIncrement(true);
        AppTableColumn ac1 = new AppTableColumn().setName("firstname").setComment("First Name").setType("varchar").setLength(20L).setRequired(true);
        AppTableColumn ac2 = new AppTableColumn().setName("lastname").setComment("Last Name").setType("varchar").setLength(10L).setDefaultValue("tom");
        List<AppTableColumn> appTableColumns = asList(ac0, ac1, ac2);

        AppTable appTable = new AppTable().setTableComment("测试表").setTableName("tname").setAppId(1L).setDsName("jsoa").setColumnMetadata(objectMapper.writeValueAsString(appTableColumns));

        mvc.perform(post("/app/table")
                        .content(objectMapper.writeValueAsString(appTable))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

    @Test
    @Transactional
    public void givenJson_whenUpdateAppTable_thenSuccess() throws Exception {
        AppTable requestBody = new AppTable()
                .setId(8L).setTableName("daxia");

        mvc.perform(post("/app/table")
                        .content(objectMapper.writeValueAsString(requestBody))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", adminAccessToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
    }

//    @Test
//    public void givenId_whenSyncAppTable_thenSuccess() throws Exception {
//        mvc.perform(post("/app/table/sync/1")
//                        .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()));
//    }

//    @Test
//    @Transactional
//    public void givenId_whenDeleteAppTable_thenSuccess() throws Exception {
//        mvc.perform(delete("/app/table").param("ids", "1,2")
//                        .header("Authorization", adminAccessToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.code").value(ResultEnum.SUCCESS.getCode()))
//                .andExpect(jsonPath("$.data").value(true));
//    }
}