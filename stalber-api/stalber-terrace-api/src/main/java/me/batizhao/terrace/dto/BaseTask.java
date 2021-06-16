package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <p> 任务查询参数基础类 </p>
 *
 * @author wws
 * @since 2021-05-18 09:18
 */
@Data
@NoArgsConstructor
@ApiModel(value = "任务查询参数基础类", description = "任务查询参数基础类")
public class BaseTask implements Serializable {

    /**
     * 任务类型 所有任务
     **/
    public final static String TASK_ALL_TYPE = "0";
    /**
     * 任务类型 流程审批任务
     **/
    public final static String TASK_CHECK_TYPE = "1";
    /**
     * 任务类型 流程传阅任务
     **/
    public final static String TASK_CIR_TYPE = "2";

    /**
     * 任务类型 所有任务状态
     **/
    public final static String TASK_ALL_STATUS = "0";
    /**
     * 任务类型 待办（带阅）任务
     **/
    public final static String TASK_TODO_STATUS = "1";
    /**
     * 任务类型 在办（在阅）任务
     **/
    public final static String TASK_DOING_STATUS = "2";

    /**
     * 任务状态类型 待办任务
     **/
    public final static String TASK_TYPE_TODO = "0";
    /**
     * 任务状态类型 已办任务
     **/
    public final static String TASK_TYPE_DONE = "1";

    /**
     * 对接接口
     **/
    public final static int TASK_QUERY_TYPE_APP = 0;
    /**
     * 平台接口
     **/
    public final static int TASK_QUERY_TYPE_MGR = 1;

    @ApiModelProperty(value = "业务系统Id", name = "businessId", hidden = true)
    private String businessId;

    @ApiModelProperty(value = "正文编号", name = "code")
    private String code;

    @ApiModelProperty(value = "正文标题", name = "title")
    private String title;

    @ApiModelProperty(value = "姓名", name = "realName")
    private String realName;

    @ApiModelProperty(value = "登录名", name = "userName")
    private String userName;
}
