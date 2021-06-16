package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author batizhao
 * @date 2021/6/15
 */
@Data
@ApiModel(value = "流程启动参数", description = "流程启动参数")
public class ProcessDTO {

    /**
     * 租户Id
     */
    @ApiModelProperty(value = "租户Id", name = "tenantId")
    private String tenantId;

    /**
     * 流程定义Id
     */
    @NotEmpty(message = "流程定义Id")
    @ApiModelProperty(value = "流程定义Id", name = "processDefinitionId")
    private String processDefinitionId;

    /**
     * 当前环节id
     **/
    @ApiModelProperty(value = "当前环节id", name = "current")
    @NotEmpty(message = "流程提交当前环节不能为空")
    private String current;

    /**
     * 业务数据
     */
    @ApiModelProperty(value = "业务数据", name = "dto")
    private ApplicationDTO dto;

    /**
     * 处理意见
     */
    @ApiModelProperty(value = "处理意见", name = "suggestion")
    private String suggestion;

    /**
     * 当前任务处理人
     */
    @NotEmpty(message = "当前任务处理人不能为空")
    @ApiModelProperty(value = "当前任务处理人", name = "userId", required = true)
    private String userId;

    /**
     * 当前任务处理人名字
     */
    @NotEmpty(message = "当前任务处理人名字")
    @ApiModelProperty(value = "当前任务处理人名字", name = "userId", required = true)
    private String userName;

    /**
     * 当前任务委托人
     */
    @ApiModelProperty(value = "当前任务委托人", name = "principal")
    private String principal;

    /**
     * 当前任务委托名字
     */
    @ApiModelProperty(value = "当前任务委托名字", name = "principal")
    private String principalName;

    /**
     * 当前任务处理人组织id
     */
    @ApiModelProperty(value = "当前任务处理人组织id", name = "orgId")
    private String orgId;

    /**
     * 当前任务处理人组织名
     */
    @ApiModelProperty(value = "当前任务处理人组织名称", name = "orgName")
    private String orgName;

    /**
     * 当前任务处理人角色id
     */
    @ApiModelProperty(value = "当前任务处理人角色id", name = "roleId")
    private String roleId;

    /**
     * 当前任务处理人角色名
     */
    @ApiModelProperty(value = "当前任务处理人角色名", name = "roleName")
    private String roleName;

    /**
     * 数据来源
     **/
    @ApiModelProperty(value = "用户采用什么提交数据：0 pc、1 手机、2 其他", name = "source")
    private Integer source;

    /**
     * 手机短信发送标示
     **/
    @ApiModelProperty(value = "手机短信发送标示: false 不发送短信、true 推送短信", name = "sendSMS")
    private Boolean sendSMS;

    /**
     * 封装提交不同环节参数
     */
    @NotEmpty(message = "下一环节提交参数不能为空")
    @ApiModelProperty(value = "当前任务处理人角色名", name = "processNodeDTO", required = true)
    private List<ProcessNodeDTO> processNodeDTO;

    public ProcessDTO() {
        this.sendSMS = false;
        this.source = 0;
    }
}
