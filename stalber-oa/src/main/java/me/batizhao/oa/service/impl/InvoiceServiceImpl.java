package me.batizhao.oa.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.batizhao.common.core.exception.NotFoundException;
import me.batizhao.oa.domain.Invoice;
import me.batizhao.oa.mapper.InvoiceMapper;
import me.batizhao.oa.service.InvoiceService;
import me.batizhao.oa.service.TaskService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 手工开票接口实现类
 *
 * @author batizhao
 * @since 2021-09-06
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements InvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;
    @Autowired
    private TaskService taskService;

    @Override
    public IPage<Invoice> findInvoices(Page<Invoice> page, Invoice invoice) {
        LambdaQueryWrapper<Invoice> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(invoice.getDeptName())) {
            wrapper.like(Invoice::getDeptName, invoice.getDeptName());
        }
        if (StringUtils.isNotBlank(invoice.getUsername())) {
            wrapper.like(Invoice::getUsername, invoice.getUsername());
        }
        return invoiceMapper.selectPage(page, wrapper);
    }

    @Override
    public List<Invoice> findInvoices(Invoice invoice) {
        LambdaQueryWrapper<Invoice> wrapper = Wrappers.lambdaQuery();
        if (StringUtils.isNotBlank(invoice.getDeptName())) {
            wrapper.like(Invoice::getDeptName, invoice.getDeptName());
        }
        if (StringUtils.isNotBlank(invoice.getUsername())) {
            wrapper.like(Invoice::getUsername, invoice.getUsername());
        }
        return invoiceMapper.selectList(wrapper);
    }

    @Override
    public Invoice findById(Long id) {
        Invoice invoice = invoiceMapper.selectById(id);

        if(invoice == null) {
            throw new NotFoundException(String.format("Record not found '%s'。", id));
        }

        return invoice;
    }

    @Override
    @Transactional
    public Invoice saveOrUpdateInvoice(Invoice invoice) {
        if (invoice.getId() == null) {
            invoice.setCreateTime(LocalDateTime.now());
            invoiceMapper.insert(invoice);
            //taskService.start(invoice.getTask().setId(invoice.getId().toString()).setTitle(invoice.getTitle()));
        } else {
            invoiceMapper.updateById(invoice);
        }

        return invoice;
    }
}
