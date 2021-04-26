package me.batizhao.ims.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.annotation.SystemLog;
import me.batizhao.common.util.R;
import me.batizhao.ims.domain.Department;
import me.batizhao.ims.domain.DepartmentLeader;
import me.batizhao.ims.service.DepartmentLeaderService;
import me.batizhao.ims.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 部门 API
 *
 * @module ims
 *
 * @author batizhao
 * @since 2021-04-25
 */
@Api(tags = "部门管理")
@RestController
@Slf4j
@Validated
@RequestMapping("ims")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private DepartmentLeaderService departmentLeaderService;

    /**
     * 查询所有部门
     * @return R
     */
    @ApiOperation(value = "查询所有部门")
    @GetMapping("/department")
    @PreAuthorize("@pms.hasPermission('ims:department:admin')")
    public R<List<Department>> handleAllDepartment(Department department) {
        return R.ok(departmentService.findDepartmentTree(department));
    }

    /**
     * 通过id查询部门
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询部门")
    @GetMapping("/department/{id}")
    @PreAuthorize("@pms.hasPermission('ims:department:admin')")
    public R<Department> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(departmentService.findById(id));
    }

    /**
     * 添加或编辑部门
     * @param department 部门
     * @return R
     */
    @ApiOperation(value = "添加或编辑部门")
    @PostMapping("/department")
    @PreAuthorize("@pms.hasPermission('ims:department:add') or @pms.hasPermission('ims:department:edit')")
    public R<Department> handleSaveOrUpdate(@Valid @ApiParam(value = "部门" , required = true) @RequestBody Department department) {
        return R.ok(departmentService.saveOrUpdateDepartment(department));
    }

    /**
     * 通过id删除部门
     * @param id ID
     * @return R
     */
    @ApiOperation(value = "通过id删除部门")
    @DeleteMapping("/department")
    @PreAuthorize("@pms.hasPermission('ims:department:delete')")
    public R<String> handleDelete(@ApiParam(value = "ID", required = true) @RequestParam Integer id) {
        return departmentService.deleteById(id) ? R.ok() : R.failed("存在子部门不允许删除！");
    }

    /**
     * 更新部门状态
     *
     * @param department 部门
     * @return R
     */
    @ApiOperation(value = "更新部门状态")
    @PostMapping("/department/status")
    @PreAuthorize("@pms.hasPermission('ims:department:admin')")
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "部门" , required = true) @RequestBody Department department) {
        return R.ok(departmentService.updateStatus(department));
    }

    /**
     * 根据用户ID查询部门
     * 返回部门集合
     *
     * @param userId 用户id
     * @return R<List<Department>>
     */
    @ApiOperation(value = "根据用户ID查询部门")
    @GetMapping(value = "/department", params = "userId")
    @PreAuthorize("@pms.hasPermission('ims:department:admin')")
    public R<List<Department>> handleDepartmentsByUserId(@ApiParam(value = "用户ID", required = true) @RequestParam("userId") @Min(1) Long userId) {
        return R.ok(departmentService.findDepartmentsByUserId(userId));
    }

    /**
     * 分配部门领导
     * 返回 true or false
     *
     * @param departmentLeaders 部门领导关联
     * @return true or false
     */
    @ApiOperation(value = "分配部门领导")
    @PostMapping(value = "/department/leader")
    @PreAuthorize("@pms.hasPermission('ims:department:admin')")
    @SystemLog
    public R<Boolean> handleAddDepartmentLeaders(@ApiParam(value = "关联菜单", required = true) @RequestBody List<DepartmentLeader> departmentLeaders) {
        return R.ok(departmentLeaderService.updateDepartmentLeaders(departmentLeaders));
    }

}
