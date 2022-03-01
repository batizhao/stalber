package me.batizhao.app.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.AppTable;
import me.batizhao.app.service.AppTableService;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.common.core.constant.PecadoConstants;
import me.batizhao.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Map;

/**
 * 应用表 API
 *
 * @module app
 *
 * @author batizhao
 * @since 2022-01-27
 */
@Tag(name = "应用表管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppTableController {

    @Autowired
    private AppTableService appTableService;

    /**
     * 分页查询应用表
     * @param page 分页对象
     * @param appTable 应用表
     * @return R
     * @real_return R<Page<AppTable>>
     */
    @Operation(description = "分页查询应用表")
    @GetMapping("/tables")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<IPage<AppTable>> handleAppTables(Page<AppTable> page, AppTable appTable) {
        return R.ok(appTableService.findAppTables(page, appTable));
    }

    /**
     * 查询应用表
     * @return R<List<AppTable>>
     */
    @Operation(description = "查询应用表")
    @GetMapping("/table")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<List<AppTable>> handleAppTables(AppTable appTable) {
        return R.ok(appTableService.findAppTables(appTable));
    }

    /**
     * 通过id查询应用表
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询应用表")
    @GetMapping("/table/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<AppTable> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appTableService.findById(id));
    }

    /**
     * 添加或编辑应用表
     * @param appTable 应用表
     * @return R
     */
    @Operation(description = "添加或编辑应用表")
    @PostMapping("/table")
    @PreAuthorize("@pms.hasPermission('app:dev:add') or @pms.hasPermission('app:dev:edit')")
    public R<AppTable> handleSaveOrUpdate(@Valid @Parameter(name = "应用表" , required = true) @RequestBody AppTable appTable) {
        return R.ok(appTableService.saveOrUpdateAppTable(appTable));
    }

    /**
     * 保存代码生成元数据
     * @param appTable 应用表
     * @return R
     */
    @Operation(description = "保存代码生成元数据")
    @PostMapping("/table/code")
    @PreAuthorize("@pms.hasPermission('app:dev:edit')")
    public R<Boolean> handleUpdateCodeMetadata(@Valid @Parameter(name = "应用表" , required = true) @RequestBody AppTable appTable) {
        return R.ok(appTableService.updateCodeMetadataById(appTable));
    }

    /**
     * 通过id删除应用表
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用表")
    @DeleteMapping("/table")
    @PreAuthorize("@pms.hasPermission('app:dev:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appTableService.removeByIds(ids));
    }

    /**
     * 查询数据源下的所有表
     *
     * @param page   分页对象
     * @param appTable   生成代码
     * @param dsName 数据源
     * @return R
     * @real_return R<Page<Code>>
     */
    @Operation(description = "查询数据源下的所有表")
    @GetMapping("/table/entity")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    @SystemLog
    public R<IPage<AppTable>> handleTables(Page<AppTable> page, AppTable appTable, String dsName) {
        return R.ok(appTableService.findTables(page, appTable, dsName));
    }

    /**
     * 导入选中的表
     * @param appTables 表元数据
     * @return
     */
    @Operation(description = "导入选中的表")
    @PostMapping("/table/import")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    @SystemLog
    public R<Boolean> handleImportTables(@RequestBody List<AppTable> appTables) {
        return R.ok(appTableService.importTables(appTables));
    }

    /**
     * 同步表到数据库
     * @param id AppTable.id
     * @return R
     */
    @Operation(description = "同步表到数据库")
    @PostMapping("/table/sync/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleSyncTable(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appTableService.syncTable(id));
    }

    /**
     * 生成代码 zip
     * @param id
     * @param response
     */
    @SneakyThrows
    @Operation(description = "生成代码zip")
    @PostMapping(value = "/table/zip/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public void handleGenerateCode4Zip(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id, HttpServletResponse response) {
        byte[] data = appTableService.downloadCode(id);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.zip", PecadoConstants.BACK_END_PROJECT));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), true, data);
    }

    /**
     * 生成代码 path
     * @param id
     * @return
     */
    @Operation(description = "生成代码path")
    @PostMapping("/table/path/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleGenerateCode4Path(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appTableService.generateCode(id));
    }

    /**
     * 预览代码
     * @param id
     * @return
     */
    @Operation(description = "预览代码")
    @GetMapping("/table/preview/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Map<String, String>> handlePreviewCode(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appTableService.previewCode(id));
    }

}
