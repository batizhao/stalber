package me.batizhao.ims.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.batizhao.common.domain.TreeNode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色数据权限 实体对象
 *
 * @author batizhao
 * @since 2021-05-28
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@ApiModel(description = "角色数据权限")
@TableName("role_department")
public class RoleDepartment implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ApiModelProperty(value="roleId")
    private Long roleId;

    /**
     *
     */
    @ApiModelProperty(value="departmentId")
    private Long departmentId;

}
