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
@RequestMapping("app")
public class DashboardMenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询当前用户菜单
     * 返回菜单树
     *
     * @return 菜单树
     */
    @Operation(description = "查询当前用户菜单")
    @GetMapping("/menu/me")
    @PreAuthorize("isAuthenticated()")
    public R<List<Menu>> handleMenuTree4Me() {
        Long userId = SecurityUtils.getUser().getUserId();
        return R.ok(menuService.findMenuTreeByUserId(userId, MenuScopeEnum.DASHBOARD.getType()));
    }

    /**
     * 查询所有菜单
     * 返回菜单树
     *
     * @return R<List<Menu>>
     */
    @Operation(description = "查询所有菜单")
    @GetMapping("/menus")
    @PreAuthorize("isAuthenticated()")
    public R<List<Menu>> handleMenuTree(Menu menu) {
        return R.ok(menuService.findMenuTree(menu, MenuScopeEnum.DASHBOARD.getType()));
    }

    /**
     * 根据应用查询菜单
     * 返回菜单树
     *
     * @return R<List<Menu>>
     */
    @Operation(description = "根据应用查询菜单")
    @GetMapping(value = "/menu", params = "appId")
    @PreAuthorize("isAuthenticated()")
    public R<List<Menu>> handleMenusByAppId(@Parameter(name = "应用ID", required = true) @RequestParam("appId") @Min(1) Long appId) {
        return R.ok(menuService.findMenusByAppId(appId));
    }

}
