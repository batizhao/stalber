package me.batizhao.terrace.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.batizhao.terrace.dto.ProcessDefinitionDTO;

/**
 * <p> 初始化流程定义视图类 </p>
 *
 * @author wws
 * @since 2020-08-10 18:58
 */
@Data
@ApiModel(value = "初始化流程定义视图类", description = "初始化流程定义视图类")
public class InitProcessDefView {

    /**
     * 流程环节信息
     **/
    @ApiModelProperty(value = "流程环节信息", name = "dto")
    private NodeView view;

    /**
     * 流程定义信息
     **/
    @ApiModelProperty(value = "流程定义信息", name = "def")
    private ProcessDefinitionDTO dto;
}
