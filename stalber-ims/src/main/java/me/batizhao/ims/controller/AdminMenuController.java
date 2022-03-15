package me.batizhao.ims.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.common.core.constant.MenuScopeEnum;
import me.batizhao.common.core.util.R;
import me.batizhao.common.core.util.SecurityUtils;
import me.batizhao.ims.domain.Menu;
import me.batizhao.ims.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 菜单管理
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-03-14
 **/
@Tag(name = "菜单管理")
@RestController
@Slf4j
@Validated
@RequestMapping("ims")
public class AdminMenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询当前用户控制台菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @Operation(description = "查询当前用户控制台菜单")
    @GetMapping("/menu/me")
    @PreAuthorize("isAuthenticated()")
    public R<List<Menu>> handleMenuTree4Me() {
        Long userId = SecurityUtils.getUser().getUserId();
        return R.ok(menuService.findMenuTreeByUserId(userId, MenuScopeEnum.ADMIN.getType()));
    }

    /**
     * 根据角色查询菜单
     * 返回菜单树
     *
     * @return R<List<Menu>>
     */
    @Operation(description = "根据角色查询菜单")
    @GetMapping(value = "/menu", params = "roleId")
    @PreAuthorize("@pms.hasPermission('ims:menu:admin')")
    public R<List<Menu>> handleMenusByRoleId(@Parameter(name = "菜单ID", required = true) @RequestParam("roleId") @Min(1) Long roleId) {
        return R.ok(menuService.findMenusByRoleId(roleId));
    }

    /**
     * 查询所有控制台菜单
     * 返回菜单树
     *
     * @return R<List<Menu>>
     */
    @Operation(description = "查询所有控制台菜单")
    @GetMapping("/menus")
    @PreAuthorize("isAuthenticated()")
    public R<List<Menu>> handleMenuTree(Menu menu) {
        return R.ok(menuService.findMenuTree(menu, MenuScopeEnum.ADMIN.getType()));
    }

    /**
     * 通过id查询菜单
     * @param id 菜单 ID
     * @return R<Menu>
     */
    @Operation(description = "通过id查询菜单")
    @GetMapping("/menu/{id}")
    @PreAuthorize("@pms.hasPermission('ims:menu:admin')")
    public R<Menu> handleMenu(@Parameter(name = "菜单ID", required = true) @PathVariable("id") @Min(1) Integer id) {
        return R.ok(menuService.findMenuById(id));
    }

    /**
     * 添加或修改菜单
     * 根据是否有ID判断是添加还是修改
     *
     * @param menu 菜单属性
     * @return R<Menu>
     */
    @Operation(description = "添加或修改菜单")
    @PostMapping("/menu")
    @PreAuthorize("@pms.hasPermission('ims:menu:add') or @pms.hasPermission('ims:menu:edit')")
    @SystemLog
    public R<Menu> handleSaveOrUpdate(@Valid @Parameter(name = "菜单", required = true) @RequestBody Menu menu) {
        return R.ok(menuService.saveOrUpdateMenu(menu));
    }

    /**
     * 删除菜单
     * 根据菜单ID删除菜单
     *
     * @return R<String>
     */
    @Operation(description = "删除菜单")
    @DeleteMapping("/menu")
    @PreAuthorize("@pms.hasPermission('ims:menu:delete')")
    @SystemLog
    public R<String> handleDelete(@Parameter(name = "菜单ID串", required = true) @RequestParam Integer id) {
        return menuService.deleteById(id) ? R.ok() : R.failed("There are sub menus that cannot be deleted!");
    }

    /**
     * 更新菜单状态
     *
     * @param menu 菜单
     * @return R<Boolean>
     */
    @Operation(description = "更新菜单状态")
    @PostMapping("/menu/status")
    @PreAuthorize("@pms.hasPermission('ims:menu:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@Parameter(name = "菜单" , required = true) @RequestBody Menu menu) {
        return R.ok(menuService.updateStatus(menu));
    }

}
