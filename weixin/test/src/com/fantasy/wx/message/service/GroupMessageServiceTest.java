package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.message.bean.GroupMessage;
import com.fantasy.wx.message.service.impl.GroupMessageService;
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
public class GroupMessageServiceTest {
    @Resource
    private GroupMessageService groupMessageService;
    @Resource
    private WeixinConfigInit weixinConfigInit;
    private static final Log logger = LogFactory.getLog(GroupMessage.class);

    @Before
    public void setUp() throws Exception {
        testSave();
    }

    @After
    public void tearDown() throws Exception {
        testDelete();
    }

    @Test
    public void testFindPager() throws Exception {
        Pager<GroupMessage> p = new Pager<GroupMessage>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQS_content", "test@"));
        Pager<GroupMessage> pager = groupMessageService.findPager(p, list);
        Assert.assertNotNull(pager.getPageItems());
        logger.debug(JSON.serialize(pager));
    }

    public void testDelete() throws Exception {
        Pager<GroupMessage> pager = groupMessageService.findPager(new Pager<GroupMessage>(), new ArrayList<PropertyFilter>());
        groupMessageService.delete(pager.getPageItems().get(0).getId());
    }

    @Test
    public void testGetOutMessage() throws Exception {
        Pager<GroupMessage> pager = groupMessageService.findPager(new Pager<GroupMessage>(), new ArrayList<PropertyFilter>());
        GroupMessage o = groupMessageService.getGroupMessage(pager.getPageItems().get(0).getId());
        Assert.assertNotNull(o);
        logger.debug(JSON.serialize(o));
    }

    public void testSave() throws Exception {
        GroupMessage groupMessage = new GroupMessage();
        groupMessage.setContent("test@");
        groupMessage.setMsgType("text");

        groupMessage.setToUsers(createOpenId());
        groupMessageService.save(groupMessage);
    }

    public List<String> createOpenId() {
        List<String> list = new ArrayList<String>();
        WeixinConfigInit.WxXmlMpInMemoryConfigStorage configProvider = (WeixinConfigInit.WxXmlMpInMemoryConfigStorage) weixinConfigInit.getWxMpConfigStorage();
        list.add(configProvider.getOpenId());
        return list;
    }

    @Test
    public void testSendGroupMessage() throws Exception {
        groupMessageService.sendTextGroupMessage(1L, "福建省快乐group");
    }

    @Test
    public void testSendOpenIdMessage() throws Exception {
        groupMessageService.sendTextOpenIdMessage(createOpenId(), "福建省快乐openId");
    }
}