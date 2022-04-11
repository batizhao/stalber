package me.batizhao.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 字典 实体对象
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "字典")
public class Dictionary implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(description="主键")
    private Long id;

    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * 代码
     */
    @Schema(description="代码")
    private String code;

    /**
     * 数据
     */
    @Schema(description="数据")
    @NotBlank(message = "data is not blank")
    private String data;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

    /**
     * 描述
     */
    @Schema(description="描述")
    private String description;

    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

    /**
     * 字典数据
     *
     * @author batizhao
     * @since 2021-02-08
     */
    @Data
    @Accessors(chain = true)
    @NoArgsConstructor
    @Schema(description = "字典数据")
    public static class DictionaryData {

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
}
