package org.jfantasy.pay.service;

import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.dao.PaymentDao;
import org.jfantasy.pay.dao.RefundDao;
import org.jfantasy.pay.product.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RefundService {

    @Autowired
    private RefundDao refundDao;
    @Autowired
    private PaymentDao paymentDao;

    public Refund get(String sn) {
        return this.refundDao.get(sn);
    }

    public void result(Refund refund, Order order) {
        this.refundDao.save(refund);
    }
}
