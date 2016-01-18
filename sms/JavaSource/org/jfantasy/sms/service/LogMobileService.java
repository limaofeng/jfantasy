package org.jfantasy.sms.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.sms.bean.LogMobile;
import org.jfantasy.sms.dao.LogMobileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LogMobileService {

    @Autowired
    private LogMobileDao logMobileDao;

    public Pager<LogMobile> findPager(Pager<LogMobile> pager, List<PropertyFilter> filters) {
        return this.logMobileDao.findPager(pager, filters);
    }

}
