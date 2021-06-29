package me.batizhao.terrace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author batizhao
 * @date 2021/6/11
 */
@Data
@ConfigurationProperties(prefix = "pecado.third-party")
public class ThirdPartyServiceProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 流程平台API地址
     */
    private String terraceServiceUrl;

    /**
     * 流程平台Token
     */
    private String token;

}
