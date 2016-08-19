package org.jfantasy.member.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.spring.mvc.error.ValidationException;
import org.jfantasy.member.bean.Invoice;
import org.jfantasy.member.bean.InvoiceItem;
import org.jfantasy.member.bean.InvoiceOrder;
import org.jfantasy.member.bean.enums.InvoiceStatus;
import org.jfantasy.member.dao.InvoiceDao;
import org.jfantasy.member.dao.InvoiceOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InvoiceService {

    @Autowired
    private InvoiceDao invoiceDao;
    @Autowired
    private InvoiceOrderDao invoiceOrderDao;

    public Pager<Invoice> findPager(Pager<Invoice> pager, List<PropertyFilter> filters) {
        return this.invoiceDao.findPager(pager,filters);
    }

    @Transactional
    public Invoice save(Invoice invoice) {
        BigDecimal amount = BigDecimal.ZERO;
        for (InvoiceItem item : invoice.getItems()) {
            InvoiceOrder order = invoiceOrderDao.get(item.getOrder().getId());
            if (order == null) {
                throw new ValidationException(102.1f, "订单信息不存在");
            }
            if (order.getStatus() != InvoiceOrder.InvoiceOrderStatus.NONE) {
                throw new ValidationException(102.2f, "订单已经申请开票,不能重复申请");
            }
            item.setInvoice(invoice);
            item.setOrder(order);

            //更新订单状态
            order.setStatus(InvoiceOrder.InvoiceOrderStatus.IN_PROGRESS);
            this.invoiceOrderDao.save(order);

            amount = amount.add(order.getInvoiceAmount());
        }
        invoice.setAmount(amount.setScale(2, 0));
        invoice.setStatus(InvoiceStatus.NONE);
        //TODO 补全信息
        /*
        invoice.setTargetId();
        invoice.setTargetType();
        invoice.setMember();
        */
        return this.invoiceDao.save(invoice);
    }

    @Transactional
    public Invoice update(Invoice invoice) {
        Invoice _invoice = this.get(invoice.getId());
        if (_invoice.getStatus() == InvoiceStatus.COMPLETE || invoice.getStatus() == InvoiceStatus.NONE) {
            throw new ValidationException(102.2f, "发票状态不符,不允许修改");
        }
        return this.invoiceDao.update(invoice, true);
    }

    @Transactional
    public void deltele(Long... ids) {
        for (Long id : ids) {
            this.invoiceDao.delete(id);
        }
    }

    public Invoice get(Long id) {
        return this.invoiceDao.get(id);
    }

}
