package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.bean.MenuWeixin;
import com.fantasy.wx.service.MenuWeiXinService;
import junit.framework.Assert;
import me.chanjar.weixin.common.api.WxConsts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class TapasMenuWeixinWeiXinServiceTest {
    private static final Log logger = LogFactory.getLog(MenuWeixin.class);

    @Autowired
    private MenuWeiXinService iMenuWeiXinService;

    @Before
    public void setUp() throws Exception {
        List<MenuWeixin> menuWeixinList = new ArrayList<MenuWeixin>();
        MenuWeixin m1 = new MenuWeixin("扫一扫", WxConsts.BUTTON_VIEW, 0, 1, "", null, null);
        MenuWeixin m2 = new MenuWeixin("文化", WxConsts.BUTTON_VIEW, 0, 2, "", null, null);
        MenuWeixin m3 = new MenuWeixin("社交", WxConsts.BUTTON_VIEW, 0, 3, "", null, null);
        menuWeixinList.add(m1);
        menuWeixinList.add(m2);
        menuWeixinList.add(m3);

        iMenuWeiXinService.saveList(menuWeixinList);
        menuWeixinList = new ArrayList<MenuWeixin>();

        //
        menuWeixinList.add(new MenuWeixin("扫一扫", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/operate/list?seatCode=TP002", null, m1));

        menuWeixinList.add(new MenuWeixin("什么是Tapas", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/cms/culture/description", null, m2));
        menuWeixinList.add(new MenuWeixin("店老板", WxConsts.BUTTON_VIEW, 1, 2, "http://42.121.56.178:8888/tapas_client/cms/culture/boss", null, m2));
        menuWeixinList.add(new MenuWeixin("西班牙旅游", WxConsts.BUTTON_VIEW, 3, 1, "http://42.121.56.178:8888/tapas_client/cms/culture/trip", null, m2));

        menuWeixinList.add(new MenuWeixin("常客", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/cms/social/regulars", null, m3));
        menuWeixinList.add(new MenuWeixin("学习西班牙语", WxConsts.BUTTON_VIEW, 1, 2, "http://42.121.56.178:8888/tapas_client/cms/social/learning", null, m3));

        iMenuWeiXinService.saveList(menuWeixinList);
    }

    @After
    public void tearDown() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("NOTNULL_parent.id"));
        List<MenuWeixin> menuWeixins = iMenuWeiXinService.findAll(list);
        Long[] ids = new Long[menuWeixins.size()];
        for (int i = 0; i < menuWeixins.size(); i++) {
            ids[i] = menuWeixins.get(i).getId();
        }
        iMenuWeiXinService.delete(ids);
        menuWeixins = iMenuWeiXinService.findAll(new ArrayList<PropertyFilter>());
        Long[] bids = new Long[menuWeixins.size()];
        for (int i = 0; i < menuWeixins.size(); i++) {
            bids[i] = menuWeixins.get(i).getId();
        }
        iMenuWeiXinService.delete(bids);
    }


    @Test
    public void testFindPager() throws Exception {
        Pager<MenuWeixin> pager = new Pager<MenuWeixin>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQI_layer", "0"));
        pager.setOrderBy("sort");
        pager.setOrders(Pager.Order.asc);

        Pager p = iMenuWeiXinService.findPager(pager, list);
        Assert.assertNotNull(p.getPageItems());
        logger.debug(JSON.serialize(p));
    }

    @Test
    public void testGet() throws Exception {
        List<MenuWeixin> menuWeixins = iMenuWeiXinService.findAll(new ArrayList<PropertyFilter>());
        MenuWeixin m = iMenuWeiXinService.get(menuWeixins.get(0).getId());
        Assert.assertNotNull(m);
        logger.debug(m);
    }

    @Test
    public void testRefresh() throws Exception {
        int r = iMenuWeiXinService.refresh();
        Assert.assertEquals(r, 0);
    }

    @Test
    public void testDeleteMenu() throws Exception {
        int r = iMenuWeiXinService.deleteMenu();
        Assert.assertEquals(r, 0);
    }
}