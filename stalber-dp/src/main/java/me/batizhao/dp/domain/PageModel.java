package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 页面模型 实体对象
 *
 * @author wws
 * @since 2022-02-18
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "页面模型")
@TableName("page_model")
public class PageModel implements Serializable {

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
     * 页面类型,首页模型index、主页模型main、列表模型list、表单模型form
     */
    @Schema(description="页面类型,首页模型index、主页模型main、列表模型list、表单模型form")
    private String type;

    /**
     * 列元数据
     */
    @Schema(description="列元数据")
    private String pageMetadata;

    /**
     * 页面事件
     */
    @Schema(description="页面事件")
    private String modelEvent;

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
     * 状态:open 激活， close 禁用
     */
    @Schema(description="状态:open 激活， close 禁用")
    private String status;

    /**
     * 备注
     */
    @Schema(description="备注")
    private String remark;

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
