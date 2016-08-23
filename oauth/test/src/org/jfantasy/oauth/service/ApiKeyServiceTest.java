package org.jfantasy.oauth.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.oauth.AuthApplicationTest;
import org.jfantasy.oauth.bean.ApiKey;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(AuthApplicationTest.class)
public class ApiKeyServiceTest {

    @Autowired
    private ApiKeyService apiKeyService;

    @Test
    public void save() throws Exception {

    }

    @Test
    public void get() throws Exception {
        Pager<ApiKey> pager = apiKeyService.findPager(new Pager<ApiKey>(),new ArrayList<PropertyFilter>());
        for(ApiKey key : pager.getPageItems()){
            System.out.println(key);
        }
    }

}
