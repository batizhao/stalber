package me.batizhao.oa.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.batizhao.oa.domain.Invoice;

import java.util.List;

/**
 * 手工开票接口类
 *
 * @author batizhao
 * @since 2021-09-06
 */
public interface InvoiceService extends IService<Invoice> {

    /**
     * 分页查询手工开票
     * @param page 分页对象
     * @param invoice 手工开票
     * @return IPage<Invoice>
     */
    IPage<Invoice> findInvoices(Page<Invoice> page, Invoice invoice);

    /**
     * 查询手工开票
     * @param invoice
     * @return List<Invoice>
     */
    List<Invoice> findInvoices(Invoice invoice);

    /**
     * 通过id查询手工开票
     * @param id id
     * @return Invoice
     */
    Invoice findById(Long id);

    /**
     * 添加或编辑手工开票
     * @param invoice 手工开票
     * @return Invoice
     */
    Invoice saveOrUpdateInvoice(Invoice invoice);
}
