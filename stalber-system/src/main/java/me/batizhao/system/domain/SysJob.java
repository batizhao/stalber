package me.batizhao.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

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
@Schema(description = "任务调度")
@TableName("job")
public class SysJob implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /**
     * id
     */
    @Schema(description="id")
    private Long id;
        
    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;
        
    /**
     * 分组
     */
    @Schema(description="分组")
    private String jobGroup;
        
    /**
     * 调用目标
     */
    @Schema(description="调用目标")
    private String invokeTarget;
        
    /**
     * cron表达式
     */
    @Schema(description="cron表达式")
    private String cronExpression;
        
    /**
     * 策略（ignore/fire/nothing）
     */
    @Schema(description="策略（ignore/fire/nothing）")
    private String misfirePolicy;
        
    /**
     * 是否并发（yes/no）
     */
    @Schema(description="是否并发（yes/no）")
    private String concurrent;
        
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
