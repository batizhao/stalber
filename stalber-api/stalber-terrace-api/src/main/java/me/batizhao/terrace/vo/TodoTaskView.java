package me.batizhao.terrace.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p> 任务视图列表 </p>
 *
 * @author wws
 * @since 2020-08-22 10:51
 */
@ApiModel(value = "任务视图列表", description = "任务视图列表")
@Data
public class TodoTaskView {

    /**
     * 任务Id
     */
    @ApiModelProperty(value = "任务Id", name = "taskId")
    private String taskId;

    @ApiModelProperty(value = "任务类型", name = "任务类型：0 待办任务 1 待阅任务")
    private String type;

    /**
     * 业务数据Id
     */
    @ApiModelProperty(value = "业务数据Id", name = "appId")
    private String appId;

    /**
     * 流程实例Id
     */
    @ApiModelProperty(value = "流程实例Id", name = "procInstId")
    private String procInstId;

    /**
     * 流程定义Id
     */
    @ApiModelProperty(value = "流程定义Id", name = "procDefId")
    private String procDefId;

    /**
     * 流程定义key
     */
    @ApiModelProperty(value = "流程定义key", name = "key")
    private String key;

    /**
     * 流程环节Key
     */
    @ApiModelProperty(value = "流程环节Key", name = "taskDefKey")
    private String taskDefKey;

    /**
     * 流程环节名称
     */
    @ApiModelProperty(value = "流程环节名称", name = "taskName")
    private String taskName;

    /**
     * 业务数据编号
     */
    @ApiModelProperty(value = "业务数据编号", name = "code")
    private String code;

    /**
     * 业务数据标题
     */
    @ApiModelProperty(value = "业务数据标题", name = "title")
    private String title;

    /**
     * 起草人
     */
    @ApiModelProperty(value = "起草人", name = "createName")
    private String createName;

    /**
     * 起草部门
     */
    @ApiModelProperty(value = "起草部门", name = "createOrgName")
    private String createOrgName;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId")
    private String userId;

    /**
     * 任务创建时间
     */
    @ApiModelProperty(value = "任务创建时间", name = "createTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String createTime;

    /**
     * 模块名称
     */
    @ApiModelProperty(value = "模块名称", name = "moduleName")
    private String moduleName;

    /**
     * 业务系统模块ID
     */
    @ApiModelProperty(value = "业务系统模块ID", name = "moduleId")
    private String moduleId;

    /**
     * 模块请求路径
     */
    @ApiModelProperty(value = "模块请求路径", name = "url")
    private String url;

    /**
     * 发件人
     */
    @ApiModelProperty(value = "发件人", name = "oldUser")
    private String oldUser;

    /**
     * 待办人列表
     */
    @ApiModelProperty(value = "待办人列表", name = "todoUserList")
    private List<String> todoUserList;

    /**
     * 在办人列表
     */
    @ApiModelProperty(value = "在办人列表", name = "doingUserList")
    private List<String> doingUserList;

    /**
     * 任务签收人
     */
    @ApiModelProperty(value = "任务签收人", name = "signer")
    private String signer;

    @ApiModelProperty(value = "任务签收时间", name = "claimTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String claimTime;

    @ApiModelProperty(value = "任务结束时间", name = "endTime")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
}
