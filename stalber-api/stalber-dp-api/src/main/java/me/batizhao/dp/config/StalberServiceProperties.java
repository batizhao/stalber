package me.batizhao.dp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author batizhao
 * @date 2021/6/11
 */
@Data
@ConfigurationProperties(prefix = "pecado.service")
public class StalberServiceProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 平台API地址
     */
    private String StalberServiceUrl;

    /**
     * 平台Token
     */
    private String token;

}
