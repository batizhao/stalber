package me.batizhao.dp.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.core.util.R;
import me.batizhao.dp.domain.CodeMeta;
import me.batizhao.dp.service.CodeMetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 生成代码元数据 API
 *
 * @module system
 *
 * @author batizhao
 * @since 2021-03-19
 */
@Tag(name = "生成代码元数据管理")
@RestController
@Slf4j
@Validated
@RequestMapping("dp")
public class CodeMetaController {

    @Autowired
    private CodeMetaService codeMetaService;

    /**
     * 通过 codeId 查询生成代码元数据
     * @param codeId code.id
     * @return R
     */
    @Operation(description = "通过 codeId 查询生成代码元数据")
    @GetMapping(value = "/code/meta", params = "codeId")
    public R<List<CodeMeta>> handleCode(@Parameter(name = "codeId" , required = true) @RequestParam @Min(1) Long codeId) {
        return R.ok(codeMetaService.findByCodeId(codeId));
    }

}