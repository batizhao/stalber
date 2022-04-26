package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 应用流程 </p>
 *
 * @author wws
 * @since 2022-02-23 10:12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用流程")
@TableName("app_process")
public class AppProcess implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 应用ID
     */
    @Schema(description="应用ID")
    private Long appId;

    /**
     * 流程定义key
     */
    @Schema(description="流程定义processKey")
    private String processKey;

    /**
     * 流程名称
     */
    @Schema(description="流程名称")
    private String name;

    /**
     * 关联表单
     */
    @Schema(description="关联表单")
    private Long formId;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 版本
     */
    @Schema(description="版本")
    private Integer version;

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
