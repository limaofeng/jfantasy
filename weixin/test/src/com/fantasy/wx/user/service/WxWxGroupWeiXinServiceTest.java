package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.bean.Group;
import com.fantasy.wx.service.GroupWeiXinService;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WxWxGroupWeiXinServiceTest {
    private static final Log logger = LogFactory.getLog(Group.class);
    @Autowired
    private GroupWeiXinService iGroupWeiXinService;
    @Resource
    private WeixinConfigInit config;

    @Before
    public void setUp() throws Exception {
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        testDelete();
    }

    public void testSave() throws Exception {
        iGroupWeiXinService.create("测试");
    }

    public void testGetAll() throws Exception {
        List<Group> list = iGroupWeiXinService.getAll();
        Assert.assertNotNull(list);
        logger.debug(JSON.serialize(list));
    }

    @Test
    public void testDelete() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("LIKES_name", "测试"));
        Pager<Group> pager = iGroupWeiXinService.findPager(new Pager<Group>(), list);
        for(int i=0;i<pager.getPageItems().size();i++){
            iGroupWeiXinService.delete(pager.getPageItems().get(i).getId());
        }

    }

    @Test
    public void testUpdate() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("LIKES_name", "测试"));
        Pager<Group> pager = iGroupWeiXinService.findPager(new Pager<Group>(), list);
        Long id = pager.getPageItems().get(0).getId();
        iGroupWeiXinService.update(id, "测试2");
        Group group = iGroupWeiXinService.getGroup(id);
        Assert.assertEquals(group.getName(), "测试2");
        logger.debug(group);

    }
    @Test
    public void testRefreshGroup() throws Exception {
        List<Group> list = iGroupWeiXinService.refreshGroup();
        Assert.assertNotNull(list);
        logger.debug(JSON.serialize(list));
    }

    @Test
    public void testMoveGroup() throws Exception {
        WeixinConfigInit.WxXmlMpInMemoryConfigStorage configProvider = (WeixinConfigInit.WxXmlMpInMemoryConfigStorage) config.getWxMpConfigStorage();
        int i = iGroupWeiXinService.moveGroup(configProvider.getOpenId(), 1L);
        Assert.assertEquals(i, 0);
    }
}