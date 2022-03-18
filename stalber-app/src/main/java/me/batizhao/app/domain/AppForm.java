package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用表单 实体对象
 *
 * @author batizhao 
 * @since 2022-02-24
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用表单")
@TableName("app_form")
public class AppForm implements Serializable {

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
     * 表单名称
     */
    @Schema(description="表单名称")
    private String name;

    /**
     * 表单key
     */
    @Schema(description="表单key")
    private String formKey;

    /**
     * 表单保存URL
     */
    @Schema(description="表单保存URL")
    private String submitURL;

    /**
     * 表单元数据
     */
    @Schema(description="表单元数据")
    private String metadata;

    /**
     * 表单描述
     */
    @Schema(description="表单描述")
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
