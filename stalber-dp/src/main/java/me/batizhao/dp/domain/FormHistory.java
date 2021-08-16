package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 单历史记录 实体对象
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "表单历史记录")
@TableName("form_history")
public class FormHistory implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @ApiModelProperty(value="主键")
    private Long id;

    /**
     * 表单key
     */
    @ApiModelProperty(value="表单key")
    private String formKey;

    /**
     * 表单元数据
     */
    @ApiModelProperty(value="表单元数据")
    private String metadata;

    /**
     * 版本
     */
    @ApiModelProperty(value="版本")
    private Long version;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

}