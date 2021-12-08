package me.batizhao.ims.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "角色")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID", example = "100")
    private Long id;

    @Schema(description = "名称", example = "管理员")
    @NotBlank(message = "name is not blank")
    @Size(min = 3, max = 30)
    private String name;

    /**
     * @mock ROLE_@string("upper", 3, 20)
     */
    @Schema(description = "代码", example = "ROLE_USER")
    @NotBlank(message = "code is not blank")
    @Size(min = 3, max = 30)
    private String code;

    @Schema(description = "说明", example = "This is admin")
    private String description;

    /**
     * 数据范围（all|custom|dept|sub|oneself）
     */
    @Schema(description="数据范围（all|custom|dept|sub|oneself）")
    private String dataScope;

    /**
     * 状态
     */
    @Schema(description="状态")
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

    /**
     * 数据权限
     */
    @Schema(description = "数据权限")
    private transient List<RoleDepartment> roleDepartments;
}
