package org.jfantasy.pay.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.pay.bean.CardDesign;
import org.jfantasy.pay.bean.Log;
import org.jfantasy.pay.bean.Transaction;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class LogService {

    private final static Class LOG_TYPE_TRANSACTION = Transaction.class;
    private final static Class LOG_TYPE_CARDDESIGN = CardDesign.class;
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

    @Transactional
    public void log(CardDesign design, String notes) {
        Log log = new Log();
        log.setOwnerType(OwnerType.card_design);
        log.setOwnerId(design.getKey());
        log.setStatus(design.getStatus().name());
        log.setNotes(notes);
        logDao.save(log);
    }

    public List<Log> logs(Class type, String sn) {
        if (type == LOG_TYPE_TRANSACTION) {
            return logDao.find(Restrictions.eq("ownerType", OwnerType.transaction), Restrictions.eq("ownerId", sn));
        } else if (type == LOG_TYPE_CARDDESIGN) {
            return logDao.find(Restrictions.eq("ownerType", OwnerType.card_design), Restrictions.eq("ownerId", sn));
        }
        return Collections.emptyList();
    }

}
