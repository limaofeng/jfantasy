package org.jfantasy.pay.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.TxLog;
import org.springframework.stereotype.Repository;

@Repository
public class TxLogDao extends HibernateDao<TxLog, Long> {

    public void save(Transaction transaction, String notes) {
        TxLog log = new TxLog();
        log.setTransaction(transaction);
        log.setStatus(transaction.getStatus());
        log.setNotes(notes);
        this.save(log);
    }

}
