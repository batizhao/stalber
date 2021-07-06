package me.batizhao.oa.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 审批 实体对象
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "审批")
@TableName("comment")
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;
        
    /**
     * 标题
     */
    @ApiModelProperty(value="标题")
    private String title;
        
    /**
     * 意见
     */
    @ApiModelProperty(value="意见")
    private String comment;

    /**
     * 发单部门
     */
    @ApiModelProperty(value="发单部门")
    private String dept;

    /**
     * 发单日期
     */
    @ApiModelProperty(value="发单日期")
    private LocalDateTime date;

    /**
     * 投资金额
     */
    @ApiModelProperty(value="投资金额")
    private BigDecimal investment;

    /**
     * 年初计划
     */
    @ApiModelProperty(value="年初计划")
    private BigDecimal plan;

    /**
     * 项目名称
     */
    @ApiModelProperty(value="项目名称")
    private String name;
        
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
