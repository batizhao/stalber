package me.batizhao.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.app.domain.AppFormHistory;
import me.batizhao.app.service.AppFormHistoryService;
import me.batizhao.app.service.AppFormService;
import me.batizhao.common.core.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * 表单历史记录 API
 *
 * @module dp
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Tag(name = "表单历史记录管理")
@RestController
@Slf4j
@Validated
@RequestMapping("app")
public class AppFormHistoryController {

    @Autowired
    private AppFormHistoryService formHistoryService;
    @Autowired
    private AppFormService formService;

    /**
     * 通过id查询表单历史记录
     * @param formKey
     * @return R
     */
    @Operation(description = "通过key查询表单历史记录")
    @GetMapping("/form/history/{formKey}")
    public R<List<AppFormHistory>> handleId(@Parameter(name = "formKey" , required = true) @PathVariable("formKey") String formKey) {
        return R.ok(formHistoryService.findByFormKey(formKey));
    }

    /**
     * 通过id恢复表单历史记录
     * @param id
     * @return R
     */
    @Operation(description = "通过id恢复表单历史记录")
    @PostMapping("/form/history/{id}")
    @PreAuthorize("@pms.hasPermission('app:dev:admin')")
    public R<Boolean> handleDelete(@Parameter(name = "ID" , required = true) @PathVariable @Min(1) Long id) {
        return R.ok(formService.revertFormById(id));
    }

}