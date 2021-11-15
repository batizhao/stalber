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
 * 模板配置 实体对象
 *
 * @author batizhao
 * @since 2021-10-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "模板配置")
@TableName("code_template")
public class CodeTemplate implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;

    /**
     * 项目key
     */
    @ApiModelProperty(value="项目key")
    private String projectKey;

    /**
     * 模板名称
     */
    @ApiModelProperty(value="模板名称")
    private String name;

    /**
     * 模板内容
     */
    @ApiModelProperty(value="模板内容")
    private String content;

    /**
     * 模板描述
     */
    @ApiModelProperty(value="模板描述")
    private String description;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    private String status;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value="修改时间")
    private LocalDateTime updateTime;
}
