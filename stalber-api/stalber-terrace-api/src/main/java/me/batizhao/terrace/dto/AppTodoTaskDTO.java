package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p> 对接待办任务查询参数 </p>
 *
 * @author wws
 * @since 2021-05-18 10:54
 */
@Data
@ApiModel(value = "对接待办任务查询参数", description = "对接待办任务查询参数")
public class AppTodoTaskDTO extends TodoTask implements Serializable {

    @ApiModelProperty(value = "第三方业务系统模块所属Id", name = "businessModuleId")
    private String businessModuleId;

    public AppTodoTaskDTO() {
        super.setQueryType("0");
    }
}
