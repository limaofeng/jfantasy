package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.bean.Menu;
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

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class TapasMenuWeiXinServiceTest {
    private static final Log logger = LogFactory.getLog(Menu.class);

    @Autowired
    private MenuWeiXinService iMenuWeiXinService;

    @Before
    public void setUp() throws Exception {
        List<Menu> menuList = new ArrayList<Menu>();
        Menu m1 = new Menu("扫一扫", WxConsts.BUTTON_VIEW, 0, 1, "", null, null);
        Menu m2 = new Menu("文化", WxConsts.BUTTON_VIEW, 0, 2, "", null, null);
        Menu m3 = new Menu("社交", WxConsts.BUTTON_VIEW, 0, 3, "", null, null);
        menuList.add(m1);
        menuList.add(m2);
        menuList.add(m3);

        iMenuWeiXinService.saveList(menuList);
        menuList = new ArrayList<Menu>();

        //
        menuList.add(new Menu("扫一扫", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/operate/list?seatCode=TP002", null, m1));

        menuList.add(new Menu("什么是Tapas", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/cms/culture/description", null, m2));
        menuList.add(new Menu("店老板", WxConsts.BUTTON_VIEW, 1, 2, "http://42.121.56.178:8888/tapas_client/cms/culture/boss", null, m2));
        menuList.add(new Menu("西班牙旅游", WxConsts.BUTTON_VIEW, 3, 1, "http://42.121.56.178:8888/tapas_client/cms/culture/trip", null, m2));

        menuList.add(new Menu("常客", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/cms/social/regulars", null, m3));
        menuList.add(new Menu("学习西班牙语", WxConsts.BUTTON_VIEW, 1, 2, "http://42.121.56.178:8888/tapas_client/cms/social/learning", null, m3));

        iMenuWeiXinService.saveList(menuList);
    }

    @After
    public void tearDown() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("NOTNULL_parent.id"));
        List<Menu> menus = iMenuWeiXinService.findAll(list);
        Long[] ids = new Long[menus.size()];
        for (int i = 0; i < menus.size(); i++) {
            ids[i] = menus.get(i).getId();
        }
        iMenuWeiXinService.delete(ids);
        menus = iMenuWeiXinService.findAll(new ArrayList<PropertyFilter>());
        Long[] bids = new Long[menus.size()];
        for (int i = 0; i < menus.size(); i++) {
            bids[i] = menus.get(i).getId();
        }
        iMenuWeiXinService.delete(bids);
    }


    @Test
    public void testFindPager() throws Exception {
        Pager<Menu> pager = new Pager<Menu>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQI_layer", "0"));
        pager.setOrderBy("sort");
        pager.setOrder(Pager.Order.asc);

        Pager p = iMenuWeiXinService.findPager(pager, list);
        Assert.assertNotNull(p.getPageItems());
        logger.debug(JSON.serialize(p));
    }

    @Test
    public void testGet() throws Exception {
        List<Menu> menus = iMenuWeiXinService.findAll(new ArrayList<PropertyFilter>());
        Menu m = iMenuWeiXinService.get(menus.get(0).getId());
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