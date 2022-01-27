package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 应用表 实体对象
 *
 * @author batizhao 
 * @since 2022-01-27
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "应用表")
@TableName("app_table")
public class AppTable implements Serializable {

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
     * 表注释
     */
    @Schema(description="表注释")
    private String tableComment;

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
     * 长度
     */
    @Schema(description="长度")
    private Integer length;

    /**
     * 小数点位数
     */
    @Schema(description="小数点位数")
    private Integer decimalLength;

    /**
     * 是否主键
     */
    @Schema(description="是否主键")
    private Integer primaryKey;

    /**
     * 是否自增
     */
    @Schema(description="是否自增")
    private Integer increment;

    /**
     * 是否必须
     */
    @Schema(description="是否必须")
    private Integer required;

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
