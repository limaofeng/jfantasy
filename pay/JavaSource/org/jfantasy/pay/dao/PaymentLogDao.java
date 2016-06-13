package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Payment;
import org.jfantasy.pay.bean.PaymentLog;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentLogDao extends HibernateDao<PaymentLog, Long> {

    public void save(Payment payment,String notes) {
        PaymentLog log = new PaymentLog();
        log.setPayment(payment);
        log.setStatus(payment.getStatus());
        log.setNotes(notes);
        log.setOrder(payment.getOrder());
        this.save(log);
    }

}
