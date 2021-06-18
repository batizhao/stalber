package me.batizhao.terrace.config;

import feign.Feign;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import lombok.RequiredArgsConstructor;
import me.batizhao.terrace.api.TerraceApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author batizhao
 * @date 2021/6/11
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(value = ThirdPartyServiceProperties.class)
@ConditionalOnProperty(name = "pecado.third-party.enabled", havingValue = "true")
public class ThirdPartyClientConfig {

    private final ThirdPartyServiceProperties thirdPartyServiceProperties;

    @Bean
    public TerraceApi terraceApi() {
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
                            "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJjdXJyZW50VGltZU1pbGxpcyI6IjE2MjQwMTM4NjE3MzkiLCJleHAiOjE2MjQwNTcwNjEsImFjY291bnQiOiJqc29hIn0.qHM1Hvs2jC4VbfZV0ImS8_y7DSP9oZ5HDQlfzxm1Yho")
                    .header("Content-Type", "application/json");
                })
                .target(TerraceApi.class, thirdPartyServiceProperties.getTerraceServiceUrl());
    }

}
