package me.batizhao.dp.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.dp.domain.FormHistory;
import me.batizhao.dp.service.FormHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 表单历史记录 API
 *
 * @module dp
 *
 * @author batizhao
 * @since 2021-08-12
 */
@Api(tags = "表单历史记录管理")
@RestController
@Slf4j
@Validated
@RequestMapping("dp")
public class FormHistoryController {

    @Autowired
    private FormHistoryService formHistoryService;

    /**
     * 通过id查询表单历史记录
     * @param formKey
     * @return R
     */
    @ApiOperation(value = "通过key查询表单历史记录")
    @GetMapping("/form/history/{formKey}")
    public R<List<FormHistory>> handleId(@ApiParam(value = "formKey" , required = true) @PathVariable("formKey") String formKey) {
        return R.ok(formHistoryService.findByFormKey(formKey));
    }

}