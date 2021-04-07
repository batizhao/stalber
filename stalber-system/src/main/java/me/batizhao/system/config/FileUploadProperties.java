package me.batizhao.system.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Data
@ConfigurationProperties(prefix = "pecado.upload")
public class FileUploadProperties {

    private String location = "/tmp";

}
