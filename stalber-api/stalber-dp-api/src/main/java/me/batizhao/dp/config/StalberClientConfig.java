package me.batizhao.dp.config;

import feign.Feign;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import me.batizhao.dp.api.StalberDpApi;
import me.batizhao.dp.exception.FeignErrorDecoder;
import org.apache.http.HttpHeaders;
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
@EnableConfigurationProperties(value = StalberServiceProperties.class)
@ConditionalOnProperty(name = "pecado.service.enabled", havingValue = "true")
public class StalberClientConfig {

    private final StalberServiceProperties stalberServiceProperties;

    @Bean
    public StalberDpApi terraceApi() {
        return Feign.builder()
                .client(new ApacheHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new FeignErrorDecoder())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(template -> {
                    template.header(
                            // not available when building PRs...
                            // https://docs.travis-ci.com/user/environment-variables/#defining-encrypted-variables-in-travisyml
                            HttpHeaders.AUTHORIZATION, "Bearer " + stalberServiceProperties.getToken())
                    .header(HttpHeaders.CONTENT_TYPE, "application/json");
                })
                .target(StalberDpApi.class, stalberServiceProperties.getStalberServiceUrl());
    }

}
