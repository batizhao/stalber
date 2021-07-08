package me.batizhao.dp.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "表单")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 表单key
     */
    @ApiModelProperty(value="表单key")
    private String formKey;

    /**
     * 表单名称
     */
    @ApiModelProperty(value="表单名称")
    private String name;

    /**
     * 表单元数据
     */
    @ApiModelProperty(value="表单元数据")
    private String metadata;

    /**
     * 表单描述
     */
    @ApiModelProperty(value="表单描述")
    private String description;
}
