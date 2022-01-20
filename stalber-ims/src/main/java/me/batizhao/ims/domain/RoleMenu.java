package me.batizhao.ims.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author batizhao
 * @since 2020-02-26
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@Schema(description = "角色菜单关联")
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "角色ID")
    private Long roleId;

    @Schema(description = "菜单ID")
    private Long menuId;

//    private String path;
//    private String roleCode;
//
//    public RoleMenu(String path, String roleCode) {
//        this.path = path;
//        this.roleCode = roleCode;
//    }
}
