package com.fantasy.wx.user.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.user.bean.WxGroup;
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
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class WxWxGroupServiceTest {
    private static final Log logger = LogFactory.getLog(WxGroup.class);
    @Resource
    private IGroupService iGroupService;
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
        iGroupService.create("测试");
    }

    public void testGetAll() throws Exception {
        List<WxGroup> list = iGroupService.getAll();
        Assert.assertNotNull(list);
        logger.debug(JSON.serialize(list));
    }

    @Test
    public void testDelete() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("LIKES_name", "测试"));
        Pager<WxGroup> pager = iGroupService.findPager(new Pager<WxGroup>(), list);
        for(int i=0;i<pager.getPageItems().size();i++){
            iGroupService.delete(pager.getPageItems().get(i).getId());
        }

    }

    @Test
    public void testUpdate() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("LIKES_name", "测试"));
        Pager<WxGroup> pager = iGroupService.findPager(new Pager<WxGroup>(), list);
        Long id = pager.getPageItems().get(0).getId();
        iGroupService.update(id, "测试2");
        WxGroup wxGroup = iGroupService.getGroup(id);
        Assert.assertEquals(wxGroup.getName(), "测试2");
        logger.debug(wxGroup);

    }
    @Test
    public void testRefreshGroup() throws Exception {
        List<WxGroup> list = iGroupService.refreshGroup();
        Assert.assertNotNull(list);
        logger.debug(JSON.serialize(list));
    }

    @Test
    public void testGetGroup1() throws Exception {
        WeixinConfigInit.WxXmlMpInMemoryConfigStorage configProvider = (WeixinConfigInit.WxXmlMpInMemoryConfigStorage) config.getWxMpConfigStorage();
        Long groupId = iGroupService.getUserGroup(configProvider.getOpenId());
        Assert.assertNotNull(groupId);
        logger.debug(groupId);
    }

    @Test
    public void testMoveGroup() throws Exception {
        WeixinConfigInit.WxXmlMpInMemoryConfigStorage configProvider = (WeixinConfigInit.WxXmlMpInMemoryConfigStorage) config.getWxMpConfigStorage();
        int i = iGroupService.moveGroup(configProvider.getOpenId(), 1L);
        Assert.assertEquals(i, 0);
    }
}