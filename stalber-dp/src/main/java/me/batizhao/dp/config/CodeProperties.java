package me.batizhao.dp.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Data
@ConfigurationProperties(prefix = "pecado.code")
public class CodeProperties {

    private String templateUrl = "/tmp/templates";

}
