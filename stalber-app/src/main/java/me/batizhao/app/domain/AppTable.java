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
     * 列元数据
     */
    @Schema(description="列元数据")
    private String columnMetadata;

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
