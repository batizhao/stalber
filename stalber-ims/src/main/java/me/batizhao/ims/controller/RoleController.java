package me.batizhao.ims.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.common.core.util.R;
import me.batizhao.ims.domain.Role;
import me.batizhao.ims.domain.RoleMenu;
import me.batizhao.ims.service.RoleMenuService;
import me.batizhao.ims.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 角色管理
 * 这里是角色管理接口的描述
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Tag(name = "角色管理")
@RestController
@Slf4j
@Validated
@RequestMapping("ims")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleMenuService roleMenuService;

    /**
     * 分页查询
     * 返回角色集合
     *
     * @return R<Page<Role>>
     * @real_return R<Page<Role>>
     */
    @Operation(description = "分页查询角色")
    @GetMapping("/roles")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public R<IPage<Role>> handleRoles(Page<Role> page, Role role) {
        return R.ok(roleService.findRoles(page, role));
    }

    /**
     * 查询所有角色
     * 返回角色集合
     *
     * @return R<List<Role>>
     */
    @Operation(description = "查询所有角色")
    @GetMapping("/role")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public R<List<Role>> handleRoles() {
        return R.ok(roleService.list());
    }

    /**
     * 通过id查询角色
     * @param id id
     * @return R<Role>
     */
    @Operation(description = "通过id查询角色")
    @GetMapping("/role/{id}")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public R<Role> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(roleService.findById(id));
    }

    /**
     * 添加或修改角色
     * 根据是否有ID判断是添加还是修改
     *
     * @param role 角色属性
     * @return R<Role>
     */
    @Operation(description = "添加或修改角色")
    @PostMapping("/role")
    @PreAuthorize("@pms.hasPermission('ims:role:add') or @pms.hasPermission('ims:role:edit')")
    @SystemLog
    public R<Role> handleSaveOrUpdate(@Valid @Parameter(name = "角色", required = true) @RequestBody Role role) {
        return R.ok(roleService.saveOrUpdateRole(role));
    }

    /**
     * 删除角色
     * 根据角色ID删除角色
     *
     * @return R<Boolean>
     */
    @Operation(description = "删除角色")
    @DeleteMapping("/role")
    @PreAuthorize("@pms.hasPermission('ims:role:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@Parameter(name = "角色ID串", required = true) @RequestParam List<Long> ids) {
        return R.ok(roleService.deleteByIds(ids));
    }

    /**
     * 更新角色状态
     *
     * @param role 角色
     * @return R<Boolean>
     */
    @Operation(description = "更新角色状态")
    @PostMapping("/role/status")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@Parameter(name = "角色" , required = true) @RequestBody Role role) {
        return R.ok(roleService.updateStatus(role));
    }

    /**
     * 根据用户ID查询角色
     * 返回角色集合
     *
     * @param userId 用户id
     * @return R<List<Role>>
     */
    @Operation(description = "根据用户ID查询角色")
    @GetMapping(value = "/role", params = "userId")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    public R<List<Role>> handleRolesByUserId(@Parameter(name = "用户ID", required = true) @RequestParam("userId") @Min(1) Long userId) {
        return R.ok(roleService.findRolesByUserId(userId));
    }

    /**
     * 分配角色权限
     * 返回 true or false
     *
     * @param roleMenuList 权限清单
     * @return R<Boolean>
     */
    @Operation(description = "分配角色权限")
    @PostMapping(value = "/role/menu")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    @SystemLog
    public R<Boolean> handleAddUserRoles(@Parameter(name = "关联菜单", required = true) @RequestBody List<RoleMenu> roleMenuList) {
        return R.ok(roleMenuService.updateRoleMenus(roleMenuList));
    }

    /**
     * 分配数据权限
     * 返回 true or false
     *
     * @param role 角色（包含数据范围roleDepartments）
     * @return R<Boolean>
     */
    @Operation(description = "分配数据权限")
    @PostMapping(value = "/role/department")
    @PreAuthorize("@pms.hasPermission('ims:role:admin')")
    @SystemLog
    public R<Boolean> handleUpdateDataScope(@Parameter(name = "角色", required = true) @RequestBody Role role) {
        return R.ok(roleService.updateDataScope(role));
    }

}
