package me.batizhao.system.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 字典类型 实体对象
 *
 * @author batizhao
 * @since 2021-02-07
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "字典类型")
public class DictType implements Serializable {

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
     */
    @Schema(description="字典数据")
    private transient List<DictData> dictDataList;
}
