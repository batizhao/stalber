package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * <p> 流程提交参数参数封装 </p>
 *
 * @author wws
 * @since 2020-08-16 20:09
 */
@Data
@NoArgsConstructor
@ApiModel(value = "流程提交参数参数封装", description = "流程提交参数参数封装")
public class SubmitProcessDTO extends ProcessDTO {

    /**
     * 任务id
     */
    @NotNull(message = "任务id不能为空")
    @ApiModelProperty(value = "任务id", name = "taskId", required = true)
    private String taskId;

    /**
     * 流程实例id
     */
    @NotNull(message = "流程实例id不能为空")
    @ApiModelProperty(value = "流程实例id", name = "procInstId", required = true)
    private String procInstId;
}
