package me.batizhao.dp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.domain.FolderTree;
import me.batizhao.common.util.R;
import me.batizhao.dp.domain.CodeTemplateDTO;
import me.batizhao.dp.service.CodeTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;
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
     * 查询模板配置
     * @return R
     * @real_return R<List<FolderTree>>
     */
    @ApiOperation(value = "查询模板配置")
    @GetMapping("/code/templates")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:admin')")
    public R<List<FolderTree>> handleCodeTemplates() {
        return R.ok(codeTemplateService.findCodeTemplateTree());
    }

    /**
     * 通过path查询模板配置
     * @param path 路径
     * @return R
     */
    @ApiOperation(value = "通过path查询模板配置")
    @GetMapping("/code/template")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:admin')")
    public R<String> handlePath(@ApiParam(value = "path" , required = true) @RequestParam @Size(min = 1) String path) {
        return R.ok(codeTemplateService.findByPath(path));
    }

    /**
     * 添加或编辑模板
     * @param codeTemplateDTO 代码
     * @return R
     */
    @ApiOperation(value = "添加或编辑模板")
    @PostMapping("/code/template")
    @PreAuthorize("@pms.hasPermission('dp:codeTemplate:add') or @pms.hasPermission('dp:codeTemplate:edit')")
    public R<Boolean> handleSaveOrUpdate(@Valid @RequestBody CodeTemplateDTO codeTemplateDTO) {
        return R.ok(codeTemplateService.saveOrUpdateCodeTemplate(codeTemplateDTO));
    }
}
