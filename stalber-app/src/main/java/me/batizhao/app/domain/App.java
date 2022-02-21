package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用 实体对象
 *
 * @author batizhao 
 * @since 2022-01-21
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用")
@TableName("app")
public class App implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * 编码
     */
    @Schema(description="编码")
    private String code;

    /**
     * 分类ID
     */
    @Schema(description="分类ID")
    private Long typeId;

    /**
     * 描述
     */
    @Schema(description="描述")
    private String description;

    /**
     * 图标
     */
    @Schema(description="图标")
    private String icon;

    /**
     * 图标背景色
     */
    @Schema(description="图标背景色")
    private String background;

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
