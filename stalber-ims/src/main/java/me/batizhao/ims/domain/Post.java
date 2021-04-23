package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 岗位 实体对象
 *
 * @author batizhao
 * @since 2021-04-22
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "岗位")
@TableName("post")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /**
     * 
     */
    @ApiModelProperty(value="")
    private Long id;
        
    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;
        
    /**
     * 编码
     */
    @ApiModelProperty(value="编码")
    private String code;
        
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Long sort;
        
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
