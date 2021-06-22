package me.batizhao.oa.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务 实体对象
 *
 * @author batizhao
 * @since 2021-06-10
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "任务")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty(value="任务ID")
    private String taskId;
        
    /**
     * 标题
     */
    @ApiModelProperty(value="标题")
    private String title;
        
    /**
     * 意见
     */
    @ApiModelProperty(value="流程实例ID")
    private String procInstId;
    
}
