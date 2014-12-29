package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.bean.UserInfo;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class UserInfoServiceTest {

    private static final Log logger = LogFactory.getLog(UserInfo.class);
    @Resource
    private IUserInfoService iUserInfoService;

    @Before
    public void setUp() throws Exception {
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        testDelete();
    }


    public void testSave() throws Exception {
        UserInfo ui = new UserInfo();
        ui.setOpenId("test");
        ui.setSex("1");
        ui.setLastMessageTime(1L);
        ui.setCity("上海");
        ui.setLanguage("zh");
        ui.setNickname("测试nick");
        ui.setSubscribeTime(new Date().getTime());
        iUserInfoService.saveArry(new UserInfo[]{ui});
    }

    public void testDelete() throws Exception {
        iUserInfoService.delete("test");
    }

    public Pager<UserInfo> testFindPager(String... openid) {
        Pager<UserInfo> pager = new Pager<UserInfo>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        if (openid.length > 0) list.add(new PropertyFilter("NES_openId", openid[0]));
        return iUserInfoService.findPager(pager, list);

    }

    @Test
    public void testRefresh() throws Exception {
        iUserInfoService.refresh();
        Pager<UserInfo> p = testFindPager("test");
        Assert.assertNotNull(p.getPageItems());
        logger.debug(JSON.serialize(p));
    }

    @Test
    public void testCountUnReadSize() throws Exception {
        Pager<UserInfo> p = testFindPager();
        iUserInfoService.countUnReadSize(p.getPageItems());
        for (UserInfo u : p.getPageItems()) {
            Assert.assertNotNull(u.getUnReadSize());
            logger.debug(u.getUnReadSize());
        }
    }

    @Test
    public void testRefreshMessage() throws Exception {
        UserInfo ui = iUserInfoService.getUserInfo("test");
        Assert.assertNotNull(ui);
        logger.debug(JSON.serialize(ui));
        iUserInfoService.refreshMessage(ui);
        Assert.assertEquals(ui.getLastLookTime(), ui.getLastMessageTime());
        logger.debug(JSON.serialize(ui));
    }
}