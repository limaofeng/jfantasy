package com.fantasy.sms.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.sms.bean.LogMobile;
import com.fantasy.sms.dao.LogMobileDao;

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
