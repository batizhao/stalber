package me.batizhao.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Data
@ConfigurationProperties(prefix = "pecado.file")
public class FileProperties {

    private String uploadLocation = "/tmp/upload";

    private String codeTemplateLocation = "/tmp/templates";

}
