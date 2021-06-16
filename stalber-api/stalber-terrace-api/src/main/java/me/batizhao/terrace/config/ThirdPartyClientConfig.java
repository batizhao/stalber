package me.batizhao.terrace.config;

import feign.Feign;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import me.batizhao.terrace.api.FlowableApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author batizhao
 * @date 2021/6/11
 */
@Configuration
public class ThirdPartyClientConfig {

    @Resource
    private ThirdPartyServiceProperties thirdPartyServiceProperties;

    @Bean
    public FlowableApi flowableApi() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .logger(new Logger.ErrorLogger())
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(template -> {
                    template.header(
                            // not available when building PRs...
                            // https://docs.travis-ci.com/user/environment-variables/#defining-encrypted-variables-in-travisyml
                            "Authorization",
                            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6IjE2MjM4NDU1ODkwMzMiLCJleHAiOjE2MjM4ODg3ODksImFjY291bnQiOiJqc29hIn0.gGq9ptpImuC_E126xDbvAGU8dZx1cdEYld9dPaYk6Mg")
                    .header("Content-Type", "application/json");
                })
                .target(FlowableApi.class, thirdPartyServiceProperties.getFlowableServiceUrl());
    }

}
