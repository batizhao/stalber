package me.batizhao.system.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class DictData implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @Schema(description="主键")
    private Long id;

    /**
     * 代码
     */
    @Schema(description="代码")
    private String code;

    /**
     * 标签
     */
    @Schema(description="标签")
    private String label;

    /**
     * 值
     */
    @Schema(description="值")
    private String value;

    /**
     * 是否默认
     */
    @Schema(description="是否默认")
    private String isDefault;

    /**
     * 排序
     */
    @Schema(description="排序")
    private Long sort;

    /**
     * 状态
     */
    @Schema(description="状态")
    private String status;

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

    
}
