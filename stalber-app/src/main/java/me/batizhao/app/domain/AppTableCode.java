package me.batizhao.app.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author batizhao
 * @date 2021/7/9
 */
@Data
@Accessors(chain = true)
public class AppTableCode {

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
     * 生成代码方式（zip压缩包 path自定义路径）
     */
    @Schema(description="生成代码方式（zip压缩包 path自定义路径）")
    private String type = "zip";

    /**
     * 后端代码路径（不填默认项目路径）
     */
    @Schema(description="后端代码路径（不填默认项目路径）")
    private String path = "/";

    /**
     * 模板类型
     */
    @Schema(description="模板类型")
    private String template = "single";

    /**
     * 关联子表的id
     */
    @Schema(description="关联子表的id")
    private Long subTableId;

    /**
     * 子表关联的属性名
     */
    @Schema(description="子表关联的属性名")
    private String subTableFkName;

    /**
     * 是否生成表单模型
     */
    @Schema(description="是否生成表单模型")
    private String form = "no";

    /**
     * 表单key
     */
    @Schema(description="表单key")
    private String formKey;

    /**
     * 表单ID
     */
    @Schema(description="表单ID")
    private Long formId;

    /**
     * 整合工作流引擎
     */
    @Schema(description="整合工作流引擎")
    private String workflow = "no";

//    /**
//     * 流程Key
//     */
//    @Schema(description="流程Key")
//    private String workflowKey;

    /**
     * 生成测试用例
     */
    @Schema(description="生成测试用例")
    private String testcase = "no";

}
