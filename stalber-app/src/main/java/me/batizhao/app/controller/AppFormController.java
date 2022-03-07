package me.batizhao.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.app.domain.AppForm;
import me.batizhao.app.service.AppFormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 应用表单 API
 *
 * @module app
 *
 * @author batizhao
 * @since 2022-02-24
 */
@Tag(name = "应用表单管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppFormController {

    @Autowired
    private AppFormService appFormService;

    /**
     * 分页查询应用表单
     * @param page 分页对象
     * @param appForm 应用表单
     * @return R
     * @real_return R<Page<AppForm>>
     */
    @Operation(description = "分页查询应用表单")
    @GetMapping("/forms")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<IPage<AppForm>> handleAppForms(Page<AppForm> page, AppForm appForm) {
        return R.ok(appFormService.findAppForms(page, appForm));
    }

    /**
     * 查询应用表单
     * @return R<List<AppForm>>
     */
    @Operation(description = "查询应用表单")
    @GetMapping("/form")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<List<AppForm>> handleAppForms(AppForm appForm) {
        return R.ok(appFormService.findAppForms(appForm));
    }

//    /**
//     * 通过id查询应用表单
//     * @param id id
//     * @return R
//     */
//    @Operation(description = "通过id查询应用表单")
//    @GetMapping("/form/{id}")
//    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
//    public R<AppForm> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
//        return R.ok(appFormService.findById(id));
//    }

    /**
     * 通过key查询表单
     * @param key key
     * @return R
     */
    @Operation(description = "通过key查询表单")
    @GetMapping(value = "/form", params = "key")
    public R<AppForm> handleKey(@Parameter(name = "key" , required = true) @RequestParam("key") @Size(min = 1) String key) {
        return R.ok(appFormService.getOne(Wrappers.<AppForm>lambdaQuery().eq(AppForm::getFormKey, key)));
    }

    /**
     * 添加或编辑应用表单
     * @param appForm 应用表单
     * @return R
     */
    @Operation(description = "添加或编辑应用表单")
    @PostMapping("/form")
    @PreAuthorize("@pms.hasPermission('app:dev:add') or @pms.hasPermission('app:dev:edit')")
    public R<AppForm> handleSaveOrUpdate(@Valid @Parameter(name = "应用表单" , required = true) @RequestBody AppForm appForm) {
        return R.ok(appFormService.saveOrUpdateAppForm(appForm));
    }

    /**
     * 通过id删除应用表单
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用表单")
    @DeleteMapping("/form")
    @PreAuthorize("@pms.hasPermission('app:dev:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appFormService.removeByIds(ids));
    }

    /**
     * 更新应用表单状态
     *
     * @param appForm 应用表单
     * @return R
     */
    @Operation(description = "更新应用表单状态")
    @PostMapping("/form/status")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "应用表单" , required = true) @RequestBody AppForm appForm) {
        return R.ok(appFormService.updateStatus(appForm));
    }

}
