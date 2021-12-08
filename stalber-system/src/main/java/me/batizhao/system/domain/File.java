package me.batizhao.system.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author batizhao
 * @date 2020/9/23
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "文件")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "文件ID", example = "100")
    private Long id;

    @Schema(description = "显示名", example = "xxx")
    @NotBlank(message = "name is not blank")
    private String name;

    @Schema(description = "文件名", example = "xxx.jpg")
    @NotBlank(message = "fileName is not blank")
    private String fileName;

    @Schema(description = "大小", example = "99")
    private Long size;

    @Schema(description = "url", example = "/path/xxx.jpg")
    private String url;

    @Schema(description = "缩略图 url", example = "/path/xxx.jpg")
    private String thumbUrl;

    @Schema(description = "操作时间")
    @NotNull(message = "createTime is not blank")
    private LocalDateTime createTime;

}
