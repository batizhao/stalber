package me.batizhao.oa.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
