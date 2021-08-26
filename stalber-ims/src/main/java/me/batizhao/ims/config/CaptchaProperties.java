package me.batizhao.ims.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author batizhao
 * @date 2021/8/25
 */
@Data
@ConfigurationProperties(prefix = "pecado.service")
public class CaptchaProperties {

    /**
     * 是否开启
     */
    private Boolean enabled;

    /**
     * 平台API地址
     */
    private String type;

}
