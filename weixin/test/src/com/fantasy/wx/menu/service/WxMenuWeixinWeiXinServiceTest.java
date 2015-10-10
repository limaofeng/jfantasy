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
public class WxMenuWeixinWeiXinServiceTest {
    private static final Log logger = LogFactory.getLog(MenuWeixin.class);

    @Autowired
    private MenuWeiXinService iMenuWeiXinService;

    @Before
    public void setUp() throws Exception {
        List<MenuWeixin> menuWeixinList = new ArrayList<MenuWeixin>();
        MenuWeixin m1 = new MenuWeixin("第一个", WxConsts.BUTTON_CLICK, 0, 1, null, "V1001_TODAY_MUSIC", null);
        MenuWeixin m2 = new MenuWeixin("第二个", WxConsts.BUTTON_VIEW, 0, 2, "http://www.baidu.com?id=21", "V1001_TODAY_MUSIC", null);
        MenuWeixin m3 = new MenuWeixin("第三个", WxConsts.BUTTON_VIEW, 0, 3, "http://www.baidu.com?id=21", null, null);
        MenuWeixin m4 = new MenuWeixin("第四个", WxConsts.BUTTON_CLICK, 0, 4, null, "V1001_TODAY_MUSIC", null);
        MenuWeixin m5 = new MenuWeixin("第五个", WxConsts.BUTTON_CLICK, 0, 5, null, "V1001_TODAY_MUSIC", null);
        MenuWeixin m6 = new MenuWeixin("第六个", WxConsts.BUTTON_CLICK, 0, 6, null, "V1001_TODAY_MUSIC", null);
        menuWeixinList.add(m1);
        menuWeixinList.add(m2);
        menuWeixinList.add(m3);
        menuWeixinList.add(m4);
        menuWeixinList.add(m5);
        menuWeixinList.add(m6);

        iMenuWeiXinService.saveList(menuWeixinList);
        menuWeixinList = new ArrayList<MenuWeixin>();

        menuWeixinList.add(new MenuWeixin("hehe1", WxConsts.BUTTON_CLICK, 1, 1, null, "V1001_GOOD", m1));
        menuWeixinList.add(new MenuWeixin("hehe2", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=2", null, m1));
        menuWeixinList.add(new MenuWeixin("hehe3", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=3", null, m1));
        menuWeixinList.add(new MenuWeixin("hehe4", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=4", null, m1));
        menuWeixinList.add(new MenuWeixin("hehe6", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=5", null, m1));
        menuWeixinList.add(new MenuWeixin("hehe5", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=6", null, m1));


        menuWeixinList.add(new MenuWeixin("hehe21", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=21", null, m2));
        menuWeixinList.add(new MenuWeixin("hehe22", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=22", null, m2));
        menuWeixinList.add(new MenuWeixin("hehe23", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=23", null, m2));
        menuWeixinList.add(new MenuWeixin("hehe24", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=24", null, m2));
        menuWeixinList.add(new MenuWeixin("hehe25", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=25", null, m2));
        menuWeixinList.add(new MenuWeixin("hehe26", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=26", null, m2));


        menuWeixinList.add(new MenuWeixin("hehe36", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=31", null, m3));
        menuWeixinList.add(new MenuWeixin("hehe32", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=32", null, m3));
        menuWeixinList.add(new MenuWeixin("hehe35", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=33", null, m3));
        menuWeixinList.add(new MenuWeixin("hehe34", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=34", null, m3));
        menuWeixinList.add(new MenuWeixin("hehe33", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=35", null, m3));
        menuWeixinList.add(new MenuWeixin("hehe31", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=36", null, m3));


        menuWeixinList.add(new MenuWeixin("hehe41", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=41", null, m4));
        menuWeixinList.add(new MenuWeixin("hehe42", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=42", null, m4));
        menuWeixinList.add(new MenuWeixin("hehe43", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=43", null, m4));
        menuWeixinList.add(new MenuWeixin("hehe44", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=44", null, m4));
        menuWeixinList.add(new MenuWeixin("hehe45", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=45", null, m4));
        menuWeixinList.add(new MenuWeixin("hehe46", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=46", null, m4));


        menuWeixinList.add(new MenuWeixin("hehe51", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=51", null, m5));
        menuWeixinList.add(new MenuWeixin("hehe52", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=52", null, m5));
        menuWeixinList.add(new MenuWeixin("hehe53", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=53", null, m5));
        menuWeixinList.add(new MenuWeixin("hehe54", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=54", null, m5));
        menuWeixinList.add(new MenuWeixin("hehe55", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=55", null, m5));
        menuWeixinList.add(new MenuWeixin("hehe56", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=56", null, m5));


        menuWeixinList.add(new MenuWeixin("hehe61", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=61", null, m6));
        menuWeixinList.add(new MenuWeixin("hehe62", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=62", null, m6));
        menuWeixinList.add(new MenuWeixin("hehe63", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=63", null, m6));
        menuWeixinList.add(new MenuWeixin("hehe64", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=64", null, m6));
        menuWeixinList.add(new MenuWeixin("hehe65", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=65", null, m6));
        menuWeixinList.add(new MenuWeixin("hehe66", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=66", null, m6));

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