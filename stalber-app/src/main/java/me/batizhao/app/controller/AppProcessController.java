package me.batizhao.app.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.AppProcess;
import me.batizhao.app.service.AppProcessService;
import me.batizhao.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * <p> 应用流程 </p>
 *
 * @author wws
 * @since 2022-02-28 15:07
 */
@Tag(name = "应用流程管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppProcessController {

    @Autowired
    private AppProcessService appProcessService;

    /**
     * 分页查询应用流程表
     * @param page 分页对象
     * @param appProcess 应用流程表
     * @return R
     * @real_return R<Page<AppProcess>>
     */
    @Operation(description = "分页查询应用流程表")
    @GetMapping("/processes")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<IPage<AppProcess>> handleAppProcess(Page<AppProcess> page, AppProcess appProcess) {
        return R.ok(appProcessService.findAppProcess(page, appProcess));
    }

    /**
     * 应用流程表
     * @return R<List<AppProcess>>
     */
    @Operation(description = "查询应用流程表")
    @GetMapping("/process")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<List<AppProcess>> handleAppProcess(AppProcess appProcess) {
        return R.ok(appProcessService.findAppProcess(appProcess));
    }

    /**
     * 通过id查询应用流程表
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询应用流程表")
    @GetMapping("/process/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<AppProcess> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(appProcessService.findById(id));
    }

    /**
     * 通过应用id与版本号获取需启动的流程信息
     * @param appId 应用Id
     * @param version 版本号
     * @return R
     */
    @Operation(description = "通过应用id与版本号获取需启动的流程信息")
    @GetMapping("/process/{appId}/{version}")
    @PreAuthorize("@pms.hasPermission('app:process:user')")
    public R<AppProcess> handleAppProcess(@Parameter(name = "appId" , required = true) @PathVariable("appId") @Min(1) Long appId,
                                          @Parameter(name = "version" , required = true) @PathVariable("version") int version) {
        return R.ok(appProcessService.findAppProcess(appId, version));
    }

    /**
     * 添加或编辑应用流程表
     * @param appProcess 应用流程表
     * @return R
     */
    @Operation(description = "添加或编辑应用流程表")
    @PostMapping("/process")
    @PreAuthorize("@pms.hasPermission('app:dev:add') or @pms.hasPermission('app:dev:edit')")
    public R<AppProcess> handleSaveOrUpdate(@Valid @Parameter(name = "应用流程表" , required = true) @RequestBody AppProcess appProcess) {
        return R.ok(appProcessService.saveOrUpdateAppProcess(appProcess));
    }

    /**
     * 通过id删除应用流程表
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除应用流程表")
    @DeleteMapping("/process")
    @PreAuthorize("@pms.hasPermission('app:dev:delete')")
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(appProcessService.removeByIds(ids));
    }
}
