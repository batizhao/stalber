package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 应用列表按钮 </p>
 *
 * @author wws
 * @since 2022-03-02 19:05
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用列表按钮")
@TableName("app_list_button")
public class AppListButton implements Serializable {
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
     * 名称
     */
    @Schema(description="名称")
    private String name;

    /**
     * 操作类型： select 查询，edit 编辑，detail 详情，delete 删除， create 新建
     */
    @Schema(description="操作类型： select 查询，edit 编辑，detail 详情，delete 删除， create 新建")
    private String operType;

    /**
     * 类型：line 行内 tools 工具栏
     */
    @Schema(description="类型：line 行内 tools 工具栏")
    private String type;

    /**
     * 样式：primary, default,dashed,danger,link
     */
    @Schema(description="样式：primary, default,dashed,danger,link")
    private String style;

    /**
     * 图标
     */
    @Schema(description="图标")
    private String icon;

    /**
     * 图标
     */
    @Schema(description="请求地址")
    private String addr;

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
