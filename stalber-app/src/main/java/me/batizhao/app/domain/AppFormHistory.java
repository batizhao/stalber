package me.batizhao.app.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 单历史记录 实体对象
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "表单历史记录")
@TableName("app_form_history")
public class AppFormHistory implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    @Schema(description="主键")
    private Long id;

    /**
     * 表单key
     */
    @Schema(description="表单key")
    private String formKey;

    /**
     * 表单元数据
     */
    @Schema(description="表单元数据")
    private String metadata;

    /**
     * 版本
     */
    @Schema(description="版本")
    private Integer version;

    /**
     * 创建时间
     */
    @Schema(description="创建时间")
    private LocalDateTime createTime;

}