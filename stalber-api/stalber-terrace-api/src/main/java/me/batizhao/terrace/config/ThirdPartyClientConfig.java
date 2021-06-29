package me.batizhao.terrace.config;

import feign.Feign;
import feign.Logger;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;
import lombok.RequiredArgsConstructor;
import me.batizhao.terrace.api.TerraceApi;
import me.batizhao.terrace.exception.FeignErrorDecoder;
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
                .errorDecoder(new FeignErrorDecoder())
                .logger(new Slf4jLogger())
                .logLevel(Logger.Level.FULL)
                .requestInterceptor(template -> {
                    template.header(
                            // not available when building PRs...
                            // https://docs.travis-ci.com/user/environment-variables/#defining-encrypted-variables-in-travisyml
                            "Authorization", thirdPartyServiceProperties.getToken())
                    .header("Content-Type", "application/json");
                })
                .target(TerraceApi.class, thirdPartyServiceProperties.getTerraceServiceUrl());
    }

}
