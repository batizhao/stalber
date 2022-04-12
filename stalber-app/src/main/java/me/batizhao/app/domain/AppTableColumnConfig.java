package me.batizhao.app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2022/4/12
 */
@Data
@Accessors(chain = true)
public class AppTableColumnConfig {

    /**
     * 属性名
     */
    @Schema(description="属性名")
    @JsonInclude(Include.NON_EMPTY)
    private String name;

    /**
     * 属性注释
     */
    @Schema(description="属性注释")
    @JsonInclude(Include.NON_EMPTY)
    private String comment;

    /**
     * 属性类型
     */
    @Schema(description="属性类型")
    @JsonInclude(Include.NON_EMPTY)
    private String type;

    /**
     * 是否必须
     */
    @Schema(description="是否必须")
    private Boolean required = false;

    /**
     * 默认值
     */
    @Schema(description="默认值")
    private String defaultValue;

}
