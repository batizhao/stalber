package me.batizhao.dp.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 生成代码元数据表
 *
 * @author batizhao
 * @since 2021-02-01
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "生成代码元数据表")
public class CodeMeta extends Model<CodeMeta> {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId
    @Schema(description="pk")
    private Long id;

    /**
     * code表ID
     */
    @Schema(description="code表ID")
    private Long codeId;

    /**
     * 列名
     */
    @Schema(description="列名")
    private String columnName;

    /**
     * 列注释
     */
    @Schema(description="列注释")
    private String columnComment;

    /**
     * 列类型
     */
    @Schema(description="列类型")
    private String columnType;

    /**
     * Java类型
     */
    @Schema(description="Java类型")
    private String javaType;

    /**
     * Java属性名
     */
    @Schema(description="Java属性名")
    private String javaField;

    /**
     * 是否主键
     */
    @Schema(description="是否主键")
    private Boolean primaryKey;

    /**
     * 是否自增
     */
    @Schema(description="是否自增")
    private Boolean increment;

    /**
     * 是否必须
     */
    @Schema(description="是否必须")
    private Boolean required;

    /**
     * 是否可插入
     */
    @Schema(description="是否可插入")
    private Boolean save;

    /**
     * 是否可编辑
     */
    @Schema(description="是否可编辑")
    private Boolean edit;

    /**
     * 是否在列表显示
     */
    @Schema(description="是否在列表显示")
    private Boolean display;

    /**
     * 是否可查询
     */
    @Schema(description="是否可查询")
    private Boolean search;

    /**
     * 查询方式（等于、不等于、大于、小于、范围）
     */
    @Schema(description="查询方式（等于、不等于、大于、小于、范围）")
    private String searchType;

    /**
     * 显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）
     */
    @Schema(description="显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）")
    private String htmlType;

    /**
     * 字典类型
     */
    @Schema(description="字典类型")
    private String dictType;

    /**
     * 排序
     */
    @Schema(description="排序")
    private Integer sort;

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
