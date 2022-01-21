package me.batizhao.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.app.domain.AppType;
import me.batizhao.app.service.AppTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 应用分类 API
 *
 * @module app
 *
 * @author batizhao
 * @since 2022-01-21
 */
@Tag(name = "应用分类管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppTypeController {

    @Autowired
    private AppTypeService appTypeService;

    /**
     * 分页查询应用分类
     * @param page 分页对象
     * @param appType 应用分类
     * @return R
     * @real_return R<Page<AppType>>
     */
    @Operation(description = "分页查询应用分类")
    @GetMapping("/types")
    @PreAuthorize("@pms.hasPermission('app:type:admin')")
    public R<IPage<AppType>> handleAppTypes(Page<AppType> page, AppType appType) {
        return R.ok(appTypeService.findAppTypes(page, appType));
    }

    /**
     * 查询应用分类
     * @return R<List<AppType>>
     */
    @Operation(description = "查询应用分类")
    @GetMapping("/type")
    @PreAuthorize("@pms.hasPermission('app:type:admin')")
    public R<List<AppType>> handleAppTypes(AppType appType) {
        return R.ok(appTypeService.findAppTypes(appType));
    }

    /**
     * 通过id查询应用分类
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询应用分类")
    @GetMapping("/type/{id}")
    @PreAuthorize("@pms.hasPermission('app:type:admin')")
    public R<AppType> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appTypeService.findById(id));
    }

    /**
     * 添加或编辑应用分类
     * @param appType 应用分类
     * @return R
     */
    @Operation(description = "添加或编辑应用分类")
    @PostMapping("/type")
    @PreAuthorize("@pms.hasPermission('app:type:add') or @pms.hasPermission('app:type:edit')")
    public R<AppType> handleSaveOrUpdate(@Valid @Parameter(name = "应用分类" , required = true) @RequestBody AppType appType) {
        return R.ok(appTypeService.saveOrUpdateAppType(appType));
    }

    /**
     * 通过id删除应用分类
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用分类")
    @DeleteMapping("/type")
    @PreAuthorize("@pms.hasPermission('app:type:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appTypeService.removeByIds(ids));
    }

    /**
     * 更新应用分类状态
     *
     * @param appType 应用分类
     * @return R
     */
    @Operation(description = "更新应用分类状态")
    @PostMapping("/type/status")
    @PreAuthorize("@pms.hasPermission('app:type:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "应用分类" , required = true) @RequestBody AppType appType) {
        return R.ok(appTypeService.updateStatus(appType));
    }

}
