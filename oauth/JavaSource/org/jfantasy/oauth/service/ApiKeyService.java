package org.jfantasy.oauth.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.oauth.bean.ApiKey;
import org.jfantasy.oauth.dao.ApiKeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApiKeyService {

    private final ApiKeyDao apiKeyDao;

    @Autowired
    public ApiKeyService(ApiKeyDao apiKeyDao) {
        this.apiKeyDao = apiKeyDao;
    }

    @Transactional
    public void save(ApiKey apiKey) {
        this.apiKeyDao.save(apiKey);
    }

    public ApiKey get(String apiKey) {
        return this.apiKeyDao.get(apiKey);
    }

    public Pager<ApiKey> findPager(Pager<ApiKey> pager, List<PropertyFilter> filter) {
        return this.apiKeyDao.findPager(pager,filter);
    }

}
