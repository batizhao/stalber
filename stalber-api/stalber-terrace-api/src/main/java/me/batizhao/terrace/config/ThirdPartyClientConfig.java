package me.batizhao.terrace.config;

import feign.Feign;
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
    private ThirdPartyServiceConfig thirdPartyServiceConfig;

    @Bean
    public FlowableApi serviceAClient() {
        return Feign.builder()
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .target(FlowableApi.class, thirdPartyServiceConfig.getFlowableServiceUrl());
    }

}
