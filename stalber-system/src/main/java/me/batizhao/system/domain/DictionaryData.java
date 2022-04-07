package me.batizhao.system.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 字典 实体对象
 *
 * @author batizhao
 * @since 2021-02-08
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "字典")
public class DictionaryData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标签
     */
    @Schema(description="标签")
    @NotBlank(message = "label is not blank")
    private String label;

    /**
     * 值
     */
    @Schema(description="值")
    @NotBlank(message = "value is not blank")
    private String value;

    /**
     * 是否默认
     */
    @Schema(description="是否默认")
    private String isDefault = "no";

    /**
     * 排序
     */
    @Schema(description="排序")
    private Long sort = 1L;
}
