package me.batizhao.app.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@Accessors(chain = true)
public class AppTableColumn {

    /**
     * 列名
     */
    @Schema(description="列名")
    @JsonInclude(Include.NON_EMPTY)
    private String name;

    /**
     * 列注释
     */
    @Schema(description="列注释")
    @JsonInclude(Include.NON_EMPTY)
    private String comment;

    /**
     * 列类型
     */
    @Schema(description="列类型")
    @JsonInclude(Include.NON_EMPTY)
    private String type;

    /**
     * 长度
     */
    @Schema(description="长度")
    private Long length;

    /**
     * 小数点位数
     */
    @Schema(description="小数点位数")
    private Integer decimal = 0;

    /**
     * 是否主键
     */
    @Schema(description="是否主键")
    private Boolean primary = false;

    /**
     * 是否自增
     */
    @Schema(description="是否自增")
    private Boolean increment = false;

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

    /**
     * Java类型
     */
    @Schema(description="Java类型")
    private String javaType;

    /**
     * Java属性名
     */
    @Schema(description="Java属性名")
    private String javaField;

    /**
     * 是否可插入
     */
    @Schema(description="是否可插入")
    private Boolean save;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Schema(description="显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String htmlType;

    /**
     * 配置项
     */
    @Schema(description="配置项")
    private List<AppTableColumnConfig> config;

}
