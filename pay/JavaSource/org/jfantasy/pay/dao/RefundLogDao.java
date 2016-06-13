package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Refund;
import org.jfantasy.pay.bean.RefundLog;
import org.springframework.stereotype.Repository;

@Repository
public class RefundLogDao  extends HibernateDao<RefundLog, Long> {

    public void save(Refund refund, String notes) {
        RefundLog log = new RefundLog();
        log.setRefund(refund);
        log.setStatus(refund.getStatus());
        log.setNotes(notes);
        log.setOrder(refund.getOrder());
        this.save(log);
    }
}
