package org.jfantasy.pay.service;

import org.hibernate.criterion.Restrictions;
import org.jfantasy.pay.bean.Log;
import org.jfantasy.pay.bean.enums.OwnerType;
import org.jfantasy.pay.dao.LogDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogService {

    @Autowired
    private LogDao logDao;

    @Transactional
    public void log(OwnerType type, Long id, String action, String notes) {
        this.log(type, id.toString(), action, notes);
    }

    @Transactional
    public void log(OwnerType type, String id, String action, String notes) {
        Log log = new Log();
        log.setOwnerType(type);
        log.setOwnerId(id);
        log.setAction(action);
        log.setNotes(notes);
        logDao.save(log);
    }

    public List<Log> logs(OwnerType type, String sn) {
        return logDao.find(Restrictions.eq("ownerType", type), Restrictions.eq("ownerId", sn));
    }

}
