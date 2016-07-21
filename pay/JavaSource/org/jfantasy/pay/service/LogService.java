package org.jfantasy.pay.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.pay.bean.CardBatch;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.Log;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogService {

    private final static Class LOG_TYPE_TRANSACTION = Transaction.class;
    private final static Class LOG_TYPE_CARDDESIGN = CardDesign.class;
    private final static Class LOG_TYPE_CARDBATCH = CardBatch.class;
    @Autowired
    private LogDao logDao;

    @Transactional
    public void log(Transaction transaction) {
        Log log = new Log();
        log.setOwnerType(OwnerType.transaction);
        log.setOwnerId(transaction.getSn());
        log.setOrderKey(transaction.get(Transaction.ORDER_KEY));
        log.setStatus(transaction.getStatus().name());
        log.setNotes(transaction.getNotes());
        logDao.save(log);
    }

    public void log(CardBatch batch, String notes) {
        Log log = new Log();
        log.setOwnerType(OwnerType.card_batch);
        log.setOwnerId(batch.getNo());
        log.setStatus(batch.getStatus().name());
        log.setNotes(notes);
        logDao.save(log);
    }

    public void log(CardDesign design, String notes) {
        Log log = new Log();
        log.setOwnerType(OwnerType.card_design);
        log.setOwnerId(design.getKey());
        log.setStatus(design.getStatus().name());
        log.setNotes(notes);
        logDao.save(log);
    }

    public List<Log> logs(OwnerType type, String sn) {
        return logDao.find(Restrictions.eq("ownerType", type), Restrictions.eq("ownerId", sn));
    }

}
