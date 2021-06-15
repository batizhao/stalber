package me.batizhao.terrace.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 业务系统应用核心数据封装 </p>
 *
 * @author wws
 * @since 2020-08-11 14:04
 */
@Data
@NoArgsConstructor
@ApiModel(value = "业务系统应用核心数据封装", description = "业务系统应用核心数据封装")
public class ApplicationDTO {

    /**
     * 业务数据Id
     */
    @ApiModelProperty(value = "业务数据Id", name = "id")
    private String id;

    /**
     * 业务标题
     */
    @ApiModelProperty(value = "业务标题", name = "title")
    private String title;

    /**
     * 业务数据编码
     */
    @ApiModelProperty(value = "业务数据编码", name = "code")
    private String code;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人", name = "creator")
    private String creator;

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
}
