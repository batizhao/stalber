package me.batizhao.dp.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.dp.domain.CodeTemplate;
import me.batizhao.dp.service.CodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 模板配置 API
 *
 * @module dp
 *
 * @author batizhao
 * @since 2021-10-12
 */
@Api(tags = "模板配置管理")
@RestController
@Slf4j
@Validated
@RequestMapping("dp")
public class CodeTemplateController {

    @Autowired
    private CodeTemplateService codeTemplateService;

    /**
     * 分页查询模板配置
     * @param page 分页对象
     * @param codeTemplate 模板配置
     * @return R
     * @real_return R<Page<CodeTemplate>>
     */
    @ApiOperation(value = "分页查询模板配置")
    @GetMapping("/code/templates")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:admin')")
    public R<IPage<CodeTemplate>> handleCodeTemplates(Page<CodeTemplate> page, CodeTemplate codeTemplate) {
        return R.ok(codeTemplateService.findCodeTemplates(page, codeTemplate));
    }

    /**
     * 查询模板配置
     * @return R<List<CodeTemplate>>
     */
    @ApiOperation(value = "查询模板配置")
    @GetMapping("/code/template")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:admin')")
    public R<List<CodeTemplate>> handleCodeTemplates(CodeTemplate codeTemplate) {
        return R.ok(codeTemplateService.findCodeTemplates(codeTemplate));
    }

    /**
     * 通过id查询模板配置
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询模板配置")
    @GetMapping("/code/template/{id}")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:admin')")
    public R<CodeTemplate> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(codeTemplateService.findById(id));
    }

    /**
     * 添加或编辑模板配置
     * @param codeTemplate 模板配置
     * @return R
     */
    @ApiOperation(value = "添加或编辑模板配置")
    @PostMapping("/code/template")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:add') or @pms.hasPermission('dp:codeTemplate:edit')")
    public R<CodeTemplate> handleSaveOrUpdate(@Valid @ApiParam(value = "模板配置" , required = true) @RequestBody CodeTemplate codeTemplate) {
        return R.ok(codeTemplateService.saveOrUpdateCodeTemplate(codeTemplate));
    }

    /**
     * 通过id删除模板配置
     * @param ids ID串
     * @return R
     */
    @ApiOperation(value = "通过id删除模板配置")
    @DeleteMapping("/code/template")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:delete')")
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(codeTemplateService.removeByIds(ids));
    }

    /**
     * 更新模板配置状态
     *
     * @param codeTemplate 模板配置
     * @return R
     */
    @ApiOperation(value = "更新模板配置状态")
    @PostMapping("/code/template/status")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:admin')")
    public R<Boolean> handleUpdateStatus(@ApiParam(value = "模板配置" , required = true) @RequestBody CodeTemplate codeTemplate) {
        return R.ok(codeTemplateService.updateStatus(codeTemplate));
    }

}
