package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 表单 实体对象
 *
 * @author batizhao
 * @since 2021-03-08
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "表单")
@TableName("form")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 表单key
     */
    @Schema(description="表单key")
    private String formKey;

    /**
     * 表单名称
     */
    @Schema(description="表单名称")
    private String name;

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
