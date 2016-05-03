package org.jfantasy.wx.config.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.wx.bean.Account;
import org.jfantasy.wx.service.AccountService;
import org.jfantasy.wx.framework.session.AccountDetails;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WeixinConfigServiceTest {
    private static final Log logger = LogFactory.getLog(Account.class);
    @Autowired
    private AccountService iConfigService;

    @Before
    public void setUp() throws Exception {
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        testDelete();
    }

    @Test
    public void testGetAll() throws Exception {
        List<AccountDetails> list = iConfigService.getAll();
        Assert.assertNotNull(list);
        logger.debug(JSON.serialize(list));
    }

    @Test
    public void testFindPager() throws Exception {
        Pager<Account> pager = new Pager<Account>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        Pager p = iConfigService.findPager(pager, list);
        Assert.assertNotNull(p.getPageItems());
        logger.debug(JSON.serialize(p));
    }

    public void testSave() throws Exception {
        Account config = new Account();
        config.setAppId("wx0e7cef7ad73417eb");
        config.setSecret("e932af31311aeebf76e21a539d9f1944");
        config.setToken("haolue_weixin");
        iConfigService.save(config);
    }

    public void testDelete() throws Exception {
        iConfigService.delete("wx0e7cef7ad73417eb");
    }

    @Test
    public void testGet() throws Exception {
        Account weixinConfig = iConfigService.get("wx0e7cef7ad73417eb");
        logger.debug(JSON.serialize(weixinConfig));
    }
}