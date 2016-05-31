package org.jfantasy.oauth.service;

import org.jfantasy.oauth.bean.Application;
import org.jfantasy.oauth.dao.ApplicationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationDao applicationDao;

    @Transactional
    public void save(Application application) {
        this.applicationDao.save(application);
    }

}
