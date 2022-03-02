package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 应用列表表头 </p>
 *
 * @author wws
 * @since 2022-03-02 18:36
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用列表表头")
@TableName("app_list_header")
public class AppListHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 列表Id
     */
    @Schema(description="列表Id")
    private Long listId;

    /**
     * 编码
     */
    @Schema(description="编码")
    private String code;

    /**
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * 列类型：string 字符串，datetime 日期， number 数字，textare 大文本，binary 二进制
     */
    @Schema(description="列类型：string 字符串，datetime 日期， number 数字，textare 大文本，binary 二进制")
    private String type;

    /**
     * 对齐方式：left 居左， middle 居中，right 居右
     */
    @Schema(description="对齐方式：left 居左， middle 居中，right 居右")
    private String alignType;

    /**
     * 是否显示：0 否 1 是
     */
    @Schema(description="是否显示：0 否 1 是")
    private Integer display;

    /**
     * 是否固定：0 否 1 是
     */
    @Schema(description="是否固定：0 否 1 是")
    private Integer fixed;

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
