package me.batizhao.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.app.domain.App;
import me.batizhao.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 应用 API
 *
 * @module app
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Tag(name = "应用管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppController {

    @Autowired
    private AppService appService;

    /**
     * 分页查询应用
     * @param page 分页对象
     * @param app 应用
     * @return R
     * @real_return R<Page<App>>
     */
    @Operation(description = "分页查询应用")
    @GetMapping("/devs")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<IPage<App>> handleApps(Page<App> page, App app) {
        return R.ok(appService.findApps(page, app));
    }

    /**
     * 查询应用
     * @return R<List<App>>
     */
    @Operation(description = "查询应用")
    @GetMapping("/dev")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<List<App>> handleApps(App app) {
        return R.ok(appService.findApps(app));
    }

    /**
     * 通过id查询应用
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询应用")
    @GetMapping("/dev/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<App> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appService.findById(id));
    }

    /**
     * 添加或编辑应用
     * @param app 应用
     * @return R
     */
    @Operation(description = "添加或编辑应用")
    @PostMapping("/dev")
    @PreAuthorize("@pms.hasPermission('app:dev:add') or @pms.hasPermission('app:dev:edit')")
    public R<App> handleSaveOrUpdate(@Valid @Parameter(name = "应用" , required = true) @RequestBody App app) {
        return R.ok(appService.saveOrUpdateApp(app));
    }

    /**
     * 通过id删除应用
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用")
    @DeleteMapping("/dev")
    @PreAuthorize("@pms.hasPermission('app:dev:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appService.removeByIds(ids));
    }

    /**
     * 更新应用状态
     *
     * @param app 应用
     * @return R
     */
    @Operation(description = "更新应用状态")
    @PostMapping("/dev/status")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "应用" , required = true) @RequestBody App app) {
        return R.ok(appService.updateStatus(app));
    }

}
