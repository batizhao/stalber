package me.batizhao.dp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.util.R;
import me.batizhao.dp.domain.Ds;
import me.batizhao.dp.service.DsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 数据源
 *
 * @module pecado-ims
 *
 * @author batizhao
 * @since 2020-10-19
 */
@Api(tags = "数据源管理")
@RestController
@Slf4j
@Validated
public class DsController {

    @Autowired
    private DsService dsService;

    /**
     * 分页查询数据源
     * @param page 分页对象
     * @param ds 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询数据源")
    @GetMapping("/dp/dss")
    @PreAuthorize("@pms.hasPermission('dp:ds:admin')")
    @SystemLog
    public R<IPage<Ds>> handleDss(Page<Ds> page, Ds ds) {
        return R.ok(dsService.findDss(page, ds));
    }

    /**
     * 查询所有数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "查询所有数据源")
    @GetMapping("/dp/ds")
    @PreAuthorize("hasRole('USER')")
    @SystemLog
    public R<List<Ds>> handleDss() {
        return R.ok(dsService.list());
    }

    /**
     * 通过id查询数据源
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询数据源")
    @GetMapping("/dp/ds/{id}")
    @PreAuthorize("@pms.hasPermission('dp:ds:admin')")
    @SystemLog
    public R<Ds> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Integer id) {
        return R.ok(dsService.findById(id));
    }

    /**
     * 添加或修改数据源
     * @param ds 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或修改数据源")
    @PostMapping("/dp/ds")
    @PreAuthorize("@pms.hasPermission('dp:ds:add') or @pms.hasPermission('dp:ds:edit')")
    @SystemLog
    public R<Ds> handleSaveOrUpdate(@Valid @ApiParam(value = "数据源" , required = true) @RequestBody Ds ds) {
        return R.ok(dsService.saveOrUpdateDs(ds));
    }

    /**
     * 通过id删除数据源
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除数据源")
    @DeleteMapping("/dp/ds")
    @PreAuthorize("@pms.hasPermission('dp:ds:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(dsService.removeByIds(ids));
    }

    /**
     * 更新数据源状态
     *
     * @param ds 数据源
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新数据源状态")
    @PostMapping("/dp/ds/status")
    @PreAuthorize("@pms.hasPermission('dp:ds:admin')")
    @SystemLog
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "数据源" , required = true) @RequestBody Ds ds) {
        return R.ok(dsService.updateStatus(ds));
    }

}
