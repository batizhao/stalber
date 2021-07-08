package me.batizhao.dp;

import lombok.extern.slf4j.Slf4j;
import me.batizhao.dp.api.StalberDpApi;
import me.batizhao.dp.config.StalberClientConfig;
import me.batizhao.dp.config.StalberServiceProperties;
import me.batizhao.dp.dto.Form;
import me.batizhao.dp.dto.R;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * @author batizhao
 * @date 2021/6/17
 */
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@Tag("api")
@Import(StalberClientConfig.class)
@EnableConfigurationProperties(value = StalberServiceProperties.class)
@TestPropertySource(properties = {"pecado.service.enabled=true",
        "pecado.service.stalber-service-url=http://localhost:8888/",
        "pecado.service.token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb2RlIjowLCJ1c2VyX2lkIjoyLCJyb2xlX2lkIjpbMV0sInVzZXJfbmFtZSI6InRvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE5ODI2MjQ0MzAsImRlcHRfaWQiOlsyXSwiYXV0aG9yaXRpZXMiOlsiaW1zOmRlcGFydG1lbnQ6YWRtaW4iLCJST0xFX1VTRVIiLCJpbXM6dXNlcjphZG1pbiJdLCJqdGkiOiI1RWJwUlZTTGZqTk9VNE1Xc2RuTVk3S0FIUUEiLCJjbGllbnRfaWQiOiJ0ZXN0IiwidXNlcm5hbWUiOiJ0b20ifQ.mVM1NJdxQeJCjZOe9EwcsbQabuDtvfTIky024j24hZY"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StalberDpFeignApiTest {

    @Autowired
    private StalberDpApi stalberDpApi;

    @Autowired
    private StalberServiceProperties stalberServiceProperties;

    @Test
    @Order(1)
    public void testServiceUrl() {
        log.info("Stalber service url: {}", stalberServiceProperties.getStalberServiceUrl());

        assertThat(stalberServiceProperties.getStalberServiceUrl(), equalTo("http://localhost:8888/"));
    }

    @Test
    public void givenKey_whenLoadProcessDefinition_thenSuccess() {
        R<Form> result = stalberDpApi.loadFormByKey("60dac2f1645458df086c00bd");

        assertThat(result.getCode(), equalTo(0));

        Form form = result.getData();
        log.info("Form: {}", form);
        assertThat(form.getFormKey(), equalTo("60dac2f1645458df086c00bd"));
    }

}
