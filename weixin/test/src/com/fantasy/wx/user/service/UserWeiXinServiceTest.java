package org.jfantasy.wx.user.service;

import junit.framework.Assert;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.security.bean.enums.Sex;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.bean.UserKey;
import org.jfantasy.wx.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class UserWeiXinServiceTest {

    private static final Log logger = LogFactory.getLog(User.class);
    @Autowired
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        testDelete();
    }


    public void testSave() throws Exception {
        User ui = new User();
        ui.setOpenId("test");
        ui.setSex(Sex.female);
        ui.setLastMessageTime(1L);
        ui.setCity("上海");
        ui.setLanguage("zh");
        ui.setNickname("测试nick");
        ui.setSubscribeTime(new Date().getTime());
    }

    public void testDelete() throws Exception {
        //iUserInfoService.deleteByOpenId("test");
    }

    public Pager<User> testFindPager(String... openid) {
        Pager<User> pager = new Pager<User>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        if (openid.length > 0) list.add(new PropertyFilter("NES_openId", openid[0]));
        return userService.findPager(pager, list);

    }

    @Test
    public void testRefresh() throws Exception {
        XStreamTransformer xStreamTransformer = new XStreamTransformer();
        InputStream is = new FileInputStream("");
        XStreamTransformer.fromXml(WxMpXmlMessage.class, is);
        userService.refresh();
        Pager<User> p = testFindPager("test");
        Assert.assertNotNull(p.getPageItems());
        logger.debug(JSON.serialize(p));
    }

    @Test
    public void testCountUnReadSize() throws Exception {
        Pager<User> p = testFindPager();
        userService.countUnReadSize(p.getPageItems());
        for (User u : p.getPageItems()) {
            Assert.assertNotNull(u.getUnReadSize());
            logger.debug(u.getUnReadSize());
        }
    }

    @Test
    public void testRefreshMessage() throws Exception {
        User ui = userService.get(new UserKey("","test"));
        Assert.assertNotNull(ui);
        logger.debug(JSON.serialize(ui));
        userService.refreshMessage(ui);
        Assert.assertEquals(ui.getLastLookTime(), ui.getLastMessageTime());
        logger.debug(JSON.serialize(ui));
    }
}