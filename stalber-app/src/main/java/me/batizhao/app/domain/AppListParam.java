package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 应用列表查询参数 </p>
 *
 * @author wws
 * @since 2022-03-02 19:05
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用列表查询参数")
@TableName("app_list_param")
public class AppListParam implements Serializable {
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
     * 参数key
     */
    @Schema(description="参数key")
    private String paramKey;

    /**
     * 参数名称
     */
    @Schema(description="参数名称")
    private String name;

    /**
     * 排序号
     */
    @Schema(description="排序号")
    private Integer sort;

    /**
     * 展示类型：string 单行文本， number 数字，datetime 日期， time 时间
     */
    @Schema(description="展示类型：string 单行文本， number 数字，datetime 日期， time 时间")
    private String showType;

    /**
     * 状态:open 激活， close 禁用
     */
    @Schema(description="状态:open 激活， close 禁用")
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
