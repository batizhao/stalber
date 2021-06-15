package me.batizhao.terrace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author batizhao
 * @date 2021/6/11
 */
@Data
@Component
@ConfigurationProperties(prefix = "third-party-service")
public class ThirdPartyServiceConfig {

    private String flowableServiceUrl;

}
