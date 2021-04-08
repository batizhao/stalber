package me.batizhao.dp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.dp.domain.Form;
import me.batizhao.dp.service.FormService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 表单 API
 *
 * @module dp
 *
 * @author batizhao
 * @since 2021-03-08
 */
@Api(tags = "表单管理")
@RestController
@Slf4j
@Validated
public class FormController {

    @Autowired
    private FormService formService;

    /**
     * 分页查询表单
     * @param page 分页对象
     * @param form 表单
     * @return ResponseInfo
     */
    @ApiOperation(value = "分页查询表单")
    @GetMapping("/dp/forms")
    @PreAuthorize("@pms.hasPermission('dp:form:admin')")
    public R<IPage<Form>> handleForms(Page<Form> page, Form form) {
        return R.ok(formService.findForms(page, form));
    }

    /**
     * 通过id查询表单
     * @param id id
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id查询表单")
    @GetMapping("/dp/form/{id}")
    @PreAuthorize("@pms.hasPermission('dp:form:admin')")
    public R<Form> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(formService.findById(id));
    }

    /**
     * 添加或编辑表单
     * @param form 表单
     * @return ResponseInfo
     */
    @ApiOperation(value = "添加或编辑表单")
    @PostMapping("/dp/form")
    @PreAuthorize("@pms.hasPermission('dp:form:add') or @pms.hasPermission('dp:form:edit')")
    public R<Form> handleSaveOrUpdate(@Valid @ApiParam(value = "表单" , required = true) @RequestBody Form form) {
        return R.ok(formService.saveOrUpdateForm(form));
    }

    /**
     * 通过id删除表单
     * @param ids ID串
     * @return ResponseInfo
     */
    @ApiOperation(value = "通过id删除表单")
    @DeleteMapping("/dp/form")
    @PreAuthorize("@pms.hasPermission('dp:form:delete')")
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(formService.removeByIds(ids));
    }

    /**
     * 更新表单状态
     *
     * @param form 表单
     * @return ResponseInfo
     */
    @ApiOperation(value = "更新表单状态")
    @PostMapping("/dp/form/status")
    @PreAuthorize("@pms.hasPermission('dp:form:admin')")
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "表单" , required = true) @RequestBody Form form) {
        return R.ok(formService.updateStatus(form));
    }

}
