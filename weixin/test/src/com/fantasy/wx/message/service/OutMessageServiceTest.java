package com.fantasy.wx.message.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.message.bean.Message;
import com.fantasy.wx.message.bean.OutMessage;
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

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class OutMessageServiceTest {
    @Resource
    private OutMessageService outMessageService;
    @Resource
    private WeixinConfigInit config;
    private static final Log logger = LogFactory.getLog(OutMessage.class);

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
        Pager<OutMessage> p=new Pager<OutMessage>();
        List<PropertyFilter> list=new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQS_content","test@"));
        Pager<OutMessage> pager=outMessageService.findPager(p,list);
        Assert.assertNotNull(pager.getPageItems());
        logger.debug(JSON.serialize(pager));
    }

    public void testDelete() throws Exception {
        Pager<OutMessage> pager=outMessageService.findPager(new Pager<OutMessage>(),new ArrayList<PropertyFilter>());
        outMessageService.delete(pager.getPageItems().get(0).getId());
    }

    @Test
    public void testGetOutMessage() throws Exception {
        Pager<OutMessage> pager=outMessageService.findPager(new Pager<OutMessage>(),new ArrayList<PropertyFilter>());
        OutMessage o=outMessageService.getOutMessage(pager.getPageItems().get(0).getId());
        Assert.assertNotNull(o);
        logger.debug(JSON.serialize(o));
    }

    public void testSave() throws Exception {
        OutMessage outMessage=new OutMessage();
        outMessage.setContent("test@");
        outMessage.setMsgType("text");

        outMessage.setToUsers(createOpenId());
        outMessageService.save(outMessage);
    }
    public List<String> createOpenId(){
        List<String> list=new ArrayList<String>();
        WeixinConfigInit.WxXmlMpInMemoryConfigStorage configProvider = (WeixinConfigInit.WxXmlMpInMemoryConfigStorage)config.getWxMpConfigStorage();
        list.add(configProvider.getOpenId());
        return list;
    }

    @Test
    public void testSendGroupMessage() throws Exception {
        outMessageService.sendGroupMessage(1L, "福建省快乐group");
    }

    @Test
    public void testSendOpenIdMessage() throws Exception {
        outMessageService.sendOpenIdMessage(createOpenId(),"福建省快乐openId");
    }
}