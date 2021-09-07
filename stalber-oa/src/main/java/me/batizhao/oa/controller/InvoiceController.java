package me.batizhao.oa.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import me.batizhao.common.util.R;
import me.batizhao.oa.domain.Invoice;
import me.batizhao.oa.domain.InvoiceAndTask;
import me.batizhao.oa.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * 手工开票 API
 *
 * @module oa
 *
 * @author batizhao
 * @since 2021-09-06
 */
@Api(tags = "手工开票管理")
@RestController
@Slf4j
@Validated
@RequestMapping("oa")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * 分页查询手工开票
     * @param page 分页对象
     * @param invoice 手工开票
     * @return R
     * @real_return R<Page<Invoice>>
     */
    @ApiOperation(value = "分页查询手工开票")
    @GetMapping("/invoices")
    @PreAuthorize("@pms.hasPermission('oa:invoice:admin')")
    public R<IPage<Invoice>> handleInvoices(Page<Invoice> page, Invoice invoice) {
        return R.ok(invoiceService.findInvoices(page, invoice));
    }

    /**
     * 查询手工开票
     * @return R<List<Invoice>>
     */
    @ApiOperation(value = "查询手工开票")
    @GetMapping("/invoice")
    @PreAuthorize("@pms.hasPermission('oa:invoice:admin')")
    public R<List<Invoice>> handleInvoices(Invoice invoice) {
        return R.ok(invoiceService.findInvoices(invoice));
    }

    /**
     * 通过id查询手工开票
     * @param id id
     * @return R
     */
    @ApiOperation(value = "通过id查询手工开票")
    @GetMapping("/invoice/{id}")
    @PreAuthorize("@pms.hasPermission('oa:invoice:admin')")
    public R<Invoice> handleId(@ApiParam(value = "ID" , required = true) @PathVariable("id") @Min(1) Long id) {
        return R.ok(invoiceService.findById(id));
    }

    /**
     * 添加或编辑手工开票
     * @param invoiceTask 手工开票
     * @return R
     */
    @ApiOperation(value = "添加或编辑手工开票")
    @PostMapping("/invoice")
    @PreAuthorize("@pms.hasPermission('oa:invoice:add') or @pms.hasPermission('oa:invoice:edit')")
    public R<Invoice> handleSaveOrUpdate(@Valid @ApiParam(value = "手工开票" , required = true) @RequestBody InvoiceAndTask invoiceTask) {
        return R.ok(invoiceService.saveOrUpdateInvoice(invoiceTask));
    }

    /**
     * 通过id删除手工开票
     * @param ids ID串
     * @return R
     */
    @ApiOperation(value = "通过id删除手工开票")
    @DeleteMapping("/invoice")
    @PreAuthorize("@pms.hasPermission('oa:invoice:delete')")
    public R<Boolean> handleDelete(@ApiParam(value = "ID串" , required = true) @RequestParam List<Long> ids) {
        return R.ok(invoiceService.removeByIds(ids));
    }

}
