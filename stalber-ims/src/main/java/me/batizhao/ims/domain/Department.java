package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.common.domain.TreeNode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 部门 实体对象
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "部门")
@TableName("department")
public class Department extends TreeNode implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 第三方ID
     */
    @ApiModelProperty(value="第三方ID")
    private String uuid;
                                                
    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;
                        
    /**
     * 全名
     */
    @ApiModelProperty(value="全名")
    private String fullName;
                        
    /**
     * 排序
     */
    @ApiModelProperty(value="排序")
    private Long sort;

    /**
     * 编码
     */
    @ApiModelProperty(value="编码")
    private String code;

    /**
     * 状态
     */
    @ApiModelProperty(value="状态")
    private String status;

    /**
     * 类型（C公司 D部门）
     */
    @ApiModelProperty(value="类型（C公司 D部门）")
    private String type;

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
            
    public Department(Integer id, Integer pid) {
        this.id = id;
        this.pid = pid;
    }

}
