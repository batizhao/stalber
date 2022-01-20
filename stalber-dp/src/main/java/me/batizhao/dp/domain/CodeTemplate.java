package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 模板配置 实体对象
 *
 * @author batizhao
 * @since 2021-10-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "模板配置")
@TableName("code_template")
public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 项目key
     */
    @Schema(description="项目key")
    private String projectKey;

    /**
     * 模板名称
     */
    @Schema(description="模板名称")
    private String name;

    /**
     * 模板内容
     */
    @Schema(description="模板内容")
    private String content;

    /**
     * 模板描述
     */
    @Schema(description="模板描述")
    private String description;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description="修改时间")
    private LocalDateTime updateTime;
}
