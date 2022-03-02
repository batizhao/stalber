package me.batizhao.dp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.domain.PageModel;
import me.batizhao.dp.service.PageModelService;
import me.batizhao.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p> 页面模型 API </p>
 *
 * @author wws
 * @since 2022-02-18 13:14
 */
@Tag(name = "页面模型管理")
@RestController
@Slf4j
@Validated
@RequestMapping("dp")
public class PageModelController {

    @Autowired
    private PageModelService pageModelService;

    /**
     * 分页查询页面模型表
     * @param page 分页对象
     * @param pageModel 页面模型表
     * @return R
     * @real_return R<Page<PageModel>>
     */
    @Operation(description = "分页查询页面模型表")
    @GetMapping("/page/models")
    @PreAuthorize("@pms.hasPermission('dp:page:model:admin')")
    public R<IPage<PageModel>> handleAppTables(Page<PageModel> page, PageModel pageModel) {
        return R.ok(pageModelService.findPageModelTables(page, pageModel));
    }

    /**
     * 页面模型表
     * @return R<List<PageModel>>
     */
    @Operation(description = "页面模型表")
    @GetMapping("/page/model")
    @PreAuthorize("@pms.hasPermission('dp:page:model:admin')")
    public R<List<PageModel>> handlePageModelTable(PageModel pageModel) {
        return R.ok(pageModelService.findPageModelTable(pageModel));
    }

    /**
     * 通过id页面模型表
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询页面模型表")
    @GetMapping("/page/model/{id}")
    @PreAuthorize("@pms.hasPermission('dp:page:model:admin')")
    public R<PageModel> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(pageModelService.getById(id));
    }

    /**
     * 添加或编辑页面模型表
     * @param pageModel 页面模型表
     * @return R
     */
    @Operation(description = "添加或编辑页面模型表")
    @PostMapping("/page/model")
    @PreAuthorize("@pms.hasPermission('dp:page:model:add') or @pms.hasPermission('page:model:dev:edit')")
    public R<PageModel> handleSaveOrUpdate(@Valid @Parameter(name = "页面模型表" , required = true) @RequestBody PageModel pageModel) {
        return R.ok(pageModelService.saveOrUpdatePageModelTable(pageModel));
    }

    /**
     * 通过id删除页面模型表
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除页面模型表")
    @DeleteMapping("/page/model")
    @PreAuthorize("@pms.hasPermission('dp:page:model:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(pageModelService.removeByIds(ids));
    }

    /**
     * 更新页面模型状态
     * @param pageModel 页面模型
     * @return R
     */
    @Operation(description = "更新页面模型状态")
    @PostMapping("/page/model/status")
    @PreAuthorize("@pms.hasPermission('dp:page:model:admin')")
    public R<Boolean> handleUpdateStatus(@Parameter(name = "页面模型" , required = true) @RequestBody PageModel pageModel) {
        return R.ok(pageModelService.updateStatus(pageModel));
    }
}
