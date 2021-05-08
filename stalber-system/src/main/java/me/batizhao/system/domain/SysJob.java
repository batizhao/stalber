package me.batizhao.system.domain;

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
 * 任务调度 实体对象
 *
 * @author batizhao
 * @since 2021-05-07
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "任务调度")
@TableName("job")
public class SysJob implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /**
     * id
     */
    @ApiModelProperty(value="id")
    private Long id;
        
    /**
     * 名称
     */
    @ApiModelProperty(value="名称")
    private String name;
        
    /**
     * 分组
     */
    @ApiModelProperty(value="分组")
    private String jobGroup;
        
    /**
     * 调用目标
     */
    @ApiModelProperty(value="调用目标")
    private String invokeTarget;
        
    /**
     * cron表达式
     */
    @ApiModelProperty(value="cron表达式")
    private String cronExpression;
        
    /**
     * 策略（ignore/fire/nothing）
     */
    @ApiModelProperty(value="策略（ignore/fire/nothing）")
    private String misfirePolicy;
        
    /**
     * 是否并发（yes/no）
     */
    @ApiModelProperty(value="是否并发（yes/no）")
    private String concurrent;
        
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
