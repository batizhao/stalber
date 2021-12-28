package me.batizhao.dp.controller;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.annotation.SystemLog;
import me.batizhao.common.core.constant.PecadoConstants;
import me.batizhao.common.core.util.R;
import me.batizhao.dp.domain.Code;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.service.CodeMetaService;
import me.batizhao.dp.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 生成代码
 * 调用这个类根据 table 生成前后端代码
 *
 * @author batizhao
 * @date 2020/10/10
 */
@Tag(name = "生成代码管理")
@RestController
@Slf4j
@Validated
@RequestMapping("dp")
public class CodeController {

    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeMetaService codeMetaService;

    /**
     * 分页查询代码
     * @param page 分页对象
     * @param code 生成代码
     * @return R
     * @real_return R<Page<Code>>
     */
    @Operation(description = "分页查询代码")
    @GetMapping("/codes")
    @PreAuthorize("@pms.hasPermission('dp:code:admin')")
    public R<IPage<Code>> handleCodes(Page<Code> page, Code code) {
        return R.ok(codeService.findCodes(page, code));
    }


    /**
     * 通过id查询生成代码
     * @param id id
     * @return R
     */
    @Operation(description = "通过id查询代码")
    @GetMapping("/code/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:admin')")
    public R<Map<String, Object>> handleId(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        Code code = codeService.findById(id);
        List<CodeMeta> codeMetas = codeMetaService.findByCodeId(id);
        List<Code> codes = codeService.list()
                .stream().filter(c -> !c.getTableName().equals(code.getTableName()))
                .collect(Collectors.toList());;

        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("codeMetas", codeMetas);
        map.put("codes", codes);
        return R.ok(map);
    }

    /**
     * 修改生成代码
     * @param code 生成代码
     * @return R
     */
    @Operation(description = "修改生成代码")
    @PostMapping("/code")
    @PreAuthorize("@pms.hasPermission('dp:code:edit')")
    @SystemLog
    public R<Code> handleUpdate(@Valid @Parameter(name = "生成代码" , required = true) @RequestBody Code code) {
        return R.ok(codeService.saveOrUpdateCode(code));
    }

    /**
     * 通过id删除生成代码
     * @param ids ID串
     * @return R
     */
    @Operation(description = "通过id删除生成代码")
    @DeleteMapping("/code")
    @PreAuthorize("@pms.hasPermission('dp:code:delete')")
    @SystemLog
    public R<Boolean> handleDelete(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(codeService.deleteByIds(ids));
    }

    /**
     * 查询数据源下的所有表
     *
     * @param page   分页对象
     * @param code   生成代码
     * @param dsName 数据源
     * @return R
     * @real_return R<Page<Code>>
     */
    @Operation(description = "查询数据源下的所有表")
    @GetMapping("/code/tables")
    @PreAuthorize("@pms.hasPermission('dp:code:admin')")
    @SystemLog
    public R<IPage<Code>> handleCodeTables(Page<Code> page, Code code, String dsName) {
        return R.ok(codeService.findTables(page, code, dsName));
    }

    /**
     * 导入选中的表
     * @param codes 表元数据
     * @return
     */
    @Operation(description = "导入选中的表")
    @PostMapping("/code/table")
    @PreAuthorize("@pms.hasPermission('dp:code:import')")
    @SystemLog
    public R<Boolean> handleImportTables(@RequestBody List<Code> codes) {
        return R.ok(codeService.importTables(codes));
    }

    /**
     * 生成代码 zip
     * @param ids
     * @param response
     */
    @SneakyThrows
    @Operation(description = "生成代码zip")
    @PostMapping(value = "/code/zip")
    @PreAuthorize("@pms.hasPermission('dp:code:gen')")
    public void handleGenerateCode4Zip(@Parameter(name = "ID串" , required = true) @RequestParam List<Long> ids, HttpServletResponse response) {
        byte[] data = codeService.downloadCode(ids);
        response.reset();
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=%s.zip", PecadoConstants.BACK_END_PROJECT));
        response.addHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(data.length));
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), true, data);
    }

    /**
     * 生成代码 path
     * @param id Code Id
     * @return
     */
    @Operation(description = "生成代码path")
    @PostMapping("/code/path/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:gen')")
    public R<Boolean> handleGenerateCode4Path(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(codeService.generateCode(id));
    }

    /**
     * 预览代码
     * @param id Code Id
     * @return
     */
    @Operation(description = "预览代码")
    @GetMapping("/code/preview/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:preview')")
    public R<Map<String, String>> handlePreviewCode(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(codeService.previewCode(id));
    }

    /**
     * 同步表元数据
     * @param id Code Id
     * @return
     */
    @Operation(description = "同步表元数据")
    @PostMapping("/code/sync/{id}")
    @PreAuthorize("@pms.hasPermission('dp:code:sync')")
    @SystemLog
    public R<Boolean> handleSyncCodeMeta(@Parameter(name = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(codeService.syncCodeMeta(id));
    }
}
