package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * <p> 流程提交参数封装 </p>
 *
 * @author wws
 * @since 2020-07-13 14:33
 */
@Data
@NoArgsConstructor
@ApiModel(value = "流程提交参数封装", description = "流程节点参数封装")
public class ProcessNodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 目标节点id
     */
    @NotEmpty(message = "提交下一节点不能为空")
    @ApiModelProperty(value = "下一节点Id", name = "target", required = true)
    private String target;

    /**
     * 目标节点id
     */
    @ApiModelProperty(value = "下一节点名称", name = "targetName")
    private String targetName;

    /**
     * 选择的下一处理人
     */
    @ApiModelProperty(value = "下一处理人集合", name = "candidate", required = true)
    private List<CandidateDTO> candidate;

    /**
     * 所选最末路由名称
     */
    @NotEmpty(message = "所选最末路由名称不能为空")
    @ApiModelProperty(value = "所选最末路由名称", name = "flowName", required = true)
    private String flowName;

    /**
     * 参数类型 0、用户参数 1、组参数 2、用户与组参数
     */
    @ApiModelProperty(value = "参数类型:0、用户参数 1、组参数 2、用户与组参数 默认是：0", name = "paramType")
    private Integer paramType = 0;

    /**
     * 操作类型：0、审批 1、送阅 2、阅毕
     **/
    @ApiModelProperty(value = "操作类型：0、审批  1、送阅 2、阅毕 默认是：0", name = "operType")
    private Integer operType = 0;

}
