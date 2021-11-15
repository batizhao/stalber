package me.batizhao.oa.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.terrace.dto.ProcessNodeDTO;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

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
     * 流程定义Id
     */
    @NotEmpty(message = "流程定义Id")
    @ApiModelProperty(value = "流程定义Id", name = "processDefinitionId")
    private String processDefinitionId;

    /**
     * 任务ID
     */
    @NotEmpty
    @ApiModelProperty(value="任务ID")
    private String taskId;
        
    /**
     * 意见
     */
    @NotEmpty
    @ApiModelProperty(value="流程实例ID")
    private String procInstId;

    /**
     * 业务id
     */
    @NotEmpty
    @ApiModelProperty(value="业务id")
    private String id;

    /**
     * 业务标题
     */
    @NotEmpty
    @ApiModelProperty(value="业务标题")
    private String title;

    /**
     * 当前节点
     */
    @ApiModelProperty(value="当前节点")
    private String current;

    /**
     * 处理意见
     */
    @ApiModelProperty(value = "处理意见", name = "suggestion")
    private String suggestion;

    /**
     * 封装提交不同环节参数
     */
    @NotEmpty(message = "下一环节提交参数不能为空")
    @ApiModelProperty(value = "当前任务处理人角色名")
    private List<ProcessNodeDTO> processNodeDTO;

}
