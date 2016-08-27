package org.jfantasy.oauth.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.oauth.AuthApplicationTest;
import org.jfantasy.oauth.bean.ApiKey;
import org.jfantasy.oauth.bean.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AuthApplicationTest.class)
public class ApiKeyServiceTest {

    @Autowired
    private ApiKeyService apiKeyService;

    @Test
    public void save() throws Exception {
        ApiKey apiKey = new ApiKey();
        apiKey.setApplication(new Application(3L));
        apiKey.setPlatform("app");
        apiKeyService.save(apiKey);

        apiKey = new ApiKey();
        apiKey.setApplication(new Application(4L));
        apiKey.setPlatform("app");
        apiKeyService.save(apiKey);

        apiKey = new ApiKey();
        apiKey.setApplication(new Application(5L));
        apiKey.setPlatform("app");
        apiKeyService.save(apiKey);

        apiKey = new ApiKey();
        apiKey.setApplication(new Application(6L));
        apiKey.setPlatform("app");
        apiKeyService.save(apiKey);
    }

    @Test
    @Transactional
    public void get() throws Exception {
        Pager<ApiKey> pager = apiKeyService.findPager(new Pager<ApiKey>(),new ArrayList<PropertyFilter>());
        for(ApiKey key : pager.getPageItems()){
            System.out.println(key);
        }
    }

}
