package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p> 应用列表 </p>
 *
 * @author wws
 * @since 2022-02-28 10:32
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用列表")
@TableName("app_list")
public class AppList implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @Schema(description="id")
    private Long id;

    /**
     * 应用ID
     */
    @Schema(description="应用ID")
    private Long appId;

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
     * 数据列表接口请求地址
     */
    @Schema(description="数据列表接口请求地址")
    private String addr;

    /**
     * 是否分页 0 否 1 是
     */
    @Schema(description="是否分页 0 否 1 是")
    private Integer page;

    /**
     * 页面大小
     */
    @Schema(description="页面大小")
    private Integer pageSize;

    /**
     * 是否行号 0 否 1 是
     */
    @Schema(description="是否行号 0 否 1 是")
    private Integer lineNum;

    /**
     * 是否横向滚动 0 否 1 是
     */
    @Schema(description="是否横向滚动 0 否 1 是")
    private Integer sideScroll;

    /**
     * 滚动宽度
     */
    @Schema(description="滚动宽度")
    private Integer scrollWidth;

    /**
     * 是否操作列 0 否 1 是
     */
    @Schema(description="是否操作列 0 否 1 是")
    private Integer operField;

    /**
     * 操作列是否固定 0 否 1 是
     */
    @Schema(description="操作列是否固定 0 否 1 是")
    private Integer fixedOperField;

    /**
     * 操作列宽度
     */
    @Schema(description="操作列宽度")
    private Integer operFieldWidth;

    /**
     * 前置事件
     */
    @Schema(description="前置事件")
    private String beforeEvent;

    /**
     * 后置事件
     */
    @Schema(description="后置事件")
    private String afterEvent;

    /**
     * 页面类型:pc 电脑端， app 移动端
     */
    @Schema(description="页面类型:pc 电脑端， app 移动端")
    private String type;

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
