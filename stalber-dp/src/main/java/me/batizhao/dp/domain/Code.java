package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 生成代码
 *
 * @author batizhao
 * @since 2021-01-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "生成代码")
public class Code extends Model<Code> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @Schema(description="id")
    private Long id;

    /**
     * 数据源
     */
    @Schema(description="数据源")
    private String dsName;

    /**
     * 表名
     */
    @Schema(description="表名")
    private String tableName;

    /**
     * 表说明
     */
    @Schema(description="表说明")
    private String tableComment;

    /**
     * 引擎
     */
    @Schema(description="引擎")
    private String engine;

    /**
     * 类名
     */
    @Schema(description="类名")
    private String className;

    /**
     * 类注释
     */
    @Schema(description="类注释")
    private String classComment;

    /**
     * 作者
     */
    @Schema(description="作者")
    private String classAuthor;

    /**
     * 包名
     */
    @Schema(description="包名")
    private String packageName;

    /**
     * 所属模块/微服务/系统名，英文
     */
    @Schema(description="所属模块/微服务/系统名，英文")
    private String moduleName;

    /**
     * API后端路由
     */
    @Schema(description="API后端路由")
    private String mappingPath;

    /**
     * 模板类型
     */
    @Schema(description="模板类型")
    private String template;

    /**
     * 父菜单ID
     */
    @Schema(description="父菜单ID")
    private Long parentMenuId;

    /**
     * 生成表单方式（meta元数据 visual可视化）
     */
    @Schema(description="生成表单方式（meta元数据 visual可视化）")
    private String form;

    /**
     * 生成代码方式（zip压缩包 path自定义路径）
     */
    @Schema(description="生成代码方式（zip压缩包 path自定义路径）")
    private String type;

    /**
     * 后端代码路径（不填默认项目路径）
     */
    @Schema(description="后端代码路径（不填默认项目路径）")
    private String path;

    /**
     * 前端代码路径
     */
    @Schema(description="前端代码路径")
    private String frontPath;

    /**
     * 关联子表的code.id
     */
    @Schema(description="关联子表的code.id")
    private Long subTableId;

    /**
     * 子表关联的属性名
     */
    @Schema(description="子表关联的属性名")
    private String subTableFkName;

    /**
     * 表单元数据
     */
    @Schema(description="表单元数据")
    private String formKey;

    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    private LocalDateTime createTime;

    /**
     * 创建时间
     */
    @Schema(description="修改时间")
    private LocalDateTime updateTime;

    /**
     * 整合工作流引擎
     */
    @Schema(description="整合工作流引擎")
    private String workflow;

    /**
     * 流程Key
     */
    @Schema(description="流程Key")
    private String workflowKey;

    /**
     * 生成测试用例
     */
    @Schema(description="生成测试用例")
    private String testcase;

    /**
     * 表元数据
     */
    @Schema(description="表元数据")
    private transient List<CodeMeta> codeMetaList;

    /**
     * 子表元数据
     */
    @Schema(description="子表元数据")
    private transient Code subCode;

    /**
     * 关联表元数据
     */
    @Schema(description="关联表元数据")
    private transient List<Code> relationCode;

}
