package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 角色数据权限 实体对象
 *
 * @author batizhao
 * @since 2021-05-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "角色数据权限")
@TableName("role_department")
public class RoleDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @Schema(description="roleId")
    private Long roleId;

    /**
     *
     */
    @Schema(description="departmentId")
    private Long departmentId;

}
