package org.jfantasy.oauth.service;

import org.jfantasy.oauth.bean.ApiKey;
import org.jfantasy.oauth.dao.ApiKeyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyDao apiKeyDao;

    @Transactional
    public void save(ApiKey apiKey) {
        this.apiKeyDao.save(apiKey);
    }

    public ApiKey get(String apiKey) {
        return this.apiKeyDao.get(apiKey);
    }
}
