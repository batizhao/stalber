package me.batizhao.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.AppList;
import me.batizhao.app.service.AppListService;
import me.batizhao.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p> 应用列表 </p>
 *
 * @author wws
 * @since 2022-03-02 20:13
 */
@Tag(name = "应用列表")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppListController {

    @Autowired
    private AppListService appListService;

    /**
     * 分页查询应用列表
     * @param page 分页对象
     * @param appList 应用列表
     * @return R
     * @real_return R<Page<AppList>>
     */
    @Operation(description = "分页查询应用列表")
    @GetMapping("/lists")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<IPage<AppList>> handleAppList(Page<AppList> page, AppList appList) {
        return R.ok(appListService.findAppList(page, appList));
    }

    /**
     * 应用列表
     * @return R<List<AppList>>
     */
    @Operation(description = "查询应用列表")
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<List<AppList>> handleAppList(AppList appList) {
        return R.ok(appListService.findAppList(appList));
    }

    /**
     * 通过id查询应用列表
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询应用列表")
    @GetMapping("/list/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<AppList> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appListService.findById(id));
    }

    /**
     * 添加或编辑应用列表
     * @param appList 应用列表
     * @return R
     */
    @Operation(description = "添加或编辑应用列表")
    @PostMapping("/list")
    @PreAuthorize("@pms.hasPermission('app:dev:add') or @pms.hasPermission('app:dev:edit')")
    public R<AppList> handleSaveOrUpdate(@Valid @Parameter(name = "应用列表" , required = true) @RequestBody AppList appList) {
        return R.ok(appListService.saveOrUpdateAppList(appList));
    }

    /**
     * 通过id删除应用列表
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用列表")
    @DeleteMapping("/list")
    @PreAuthorize("@pms.hasPermission('app:dev:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appListService.removeByIds(ids));
    }

    /**
     * 更新应用列表状态
     * @param appList 应用列表
     * @return R
     */
    @Operation(description = "更新应用列表状态")
    @PostMapping("/list/status")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "应用列表" , required = true) @RequestBody AppList appList) {
        return R.ok(appListService.updateStatus(appList));
    }
}
