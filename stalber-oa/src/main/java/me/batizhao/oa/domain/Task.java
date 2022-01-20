package me.batizhao.oa.domain;


import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "任务")
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流程定义Id
     */
    @NotEmpty(message = "流程定义Id")
    @Schema(description = "流程定义Id")
    private String processDefinitionId;

    /**
     * 任务ID
     */
    @NotEmpty
    @Schema(description="任务ID")
    private String taskId;
        
    /**
     * 意见
     */
    @NotEmpty
    @Schema(description="流程实例ID")
    private String procInstId;

    /**
     * 业务id
     */
    @NotEmpty
    @Schema(description="业务id")
    private String id;

    /**
     * 业务标题
     */
    @NotEmpty
    @Schema(description="业务标题")
    private String title;

    /**
     * 当前节点
     */
    @Schema(description="当前节点")
    private String current;

    /**
     * 处理意见
     */
    @Schema(description = "处理意见")
    private String suggestion;

    /**
     * 封装提交不同环节参数
     */
    @NotEmpty(message = "下一环节提交参数不能为空")
    @Schema(description = "当前任务处理人角色名")
    private List<ProcessNodeDTO> processNodeDTO;

}
