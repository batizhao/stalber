package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p> 流程提交下一节点候选人参数封装 </p>
 *
 * @author wws
 * @since 2020-4-13
 */
@Data
@NoArgsConstructor
@ApiModel(value = "流程提交下一节点候选人参数封装", description = "流程提交下一节点候选人参数封装")
public class CandidateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 候选人ID
     */
    @NotNull(message = "候选人Id不能为空")
    @ApiModelProperty(value = "候选人Id", name = "userId", required = true)
    private String userId;

    /**
     * 候选人姓名
     */
    @ApiModelProperty(value = "候选人姓名", name = "userName")
    private String userName;

    /**
     * 候选人所属组织id
     */
    @NotNull(message = "候选人所属组织id不能为空")
    @ApiModelProperty(value = "候选人所属组织id", name = "orgId", required = true)
    private String orgId;

    /**
     * 候选人所属组织名称
     */
    @ApiModelProperty(value = "候选人所属组织名称", name = "orgId")
    private String orgName;

    /**
     * 候选人所属角色id
     */
    @ApiModelProperty(value = "候选人所属角色id", name = "roleId")
    private String roleId;

    /**
     * 候选人所属角色名
     */
    @ApiModelProperty(value = "候选人所属角色名", name = "roleName")
    private String roleName;

    /**
     * 委托人ID
     */
    @ApiModelProperty(value = "候选人的委托人id", name = "principal")
    private String principal;

    /**
     * 委托人姓名
     */
    @ApiModelProperty(value = "候选人的委托人名字", name = "principalName")
    private String principalName;
}
