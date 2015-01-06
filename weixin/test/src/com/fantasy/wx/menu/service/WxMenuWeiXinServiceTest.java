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
public class WxMenuWeiXinServiceTest {
    private static final Log logger = LogFactory.getLog(Menu.class);

    @Autowired
    private MenuWeiXinService iMenuWeiXinService;

    @Before
    public void setUp() throws Exception {
        List<Menu> menuList = new ArrayList<Menu>();
        Menu m1 = new Menu("第一个", WxConsts.BUTTON_CLICK, 0, 1, null, "V1001_TODAY_MUSIC", null);
        Menu m2 = new Menu("第二个", WxConsts.BUTTON_VIEW, 0, 2, "http://www.baidu.com?id=21", "V1001_TODAY_MUSIC", null);
        Menu m3 = new Menu("第三个", WxConsts.BUTTON_VIEW, 0, 3, "http://www.baidu.com?id=21", null, null);
        Menu m4 = new Menu("第四个", WxConsts.BUTTON_CLICK, 0, 4, null, "V1001_TODAY_MUSIC", null);
        Menu m5 = new Menu("第五个", WxConsts.BUTTON_CLICK, 0, 5, null, "V1001_TODAY_MUSIC", null);
        Menu m6 = new Menu("第六个", WxConsts.BUTTON_CLICK, 0, 6, null, "V1001_TODAY_MUSIC", null);
        menuList.add(m1);
        menuList.add(m2);
        menuList.add(m3);
        menuList.add(m4);
        menuList.add(m5);
        menuList.add(m6);

        iMenuWeiXinService.saveList(menuList);
        menuList = new ArrayList<Menu>();

        menuList.add(new Menu("hehe1", WxConsts.BUTTON_CLICK, 1, 1, null, "V1001_GOOD", m1));
        menuList.add(new Menu("hehe2", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=2", null, m1));
        menuList.add(new Menu("hehe3", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=3", null, m1));
        menuList.add(new Menu("hehe4", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=4", null, m1));
        menuList.add(new Menu("hehe6", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=5", null, m1));
        menuList.add(new Menu("hehe5", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=6", null, m1));


        menuList.add(new Menu("hehe21", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=21", null, m2));
        menuList.add(new Menu("hehe22", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=22", null, m2));
        menuList.add(new Menu("hehe23", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=23", null, m2));
        menuList.add(new Menu("hehe24", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=24", null, m2));
        menuList.add(new Menu("hehe25", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=25", null, m2));
        menuList.add(new Menu("hehe26", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=26", null, m2));


        menuList.add(new Menu("hehe36", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=31", null, m3));
        menuList.add(new Menu("hehe32", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=32", null, m3));
        menuList.add(new Menu("hehe35", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=33", null, m3));
        menuList.add(new Menu("hehe34", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=34", null, m3));
        menuList.add(new Menu("hehe33", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=35", null, m3));
        menuList.add(new Menu("hehe31", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=36", null, m3));


        menuList.add(new Menu("hehe41", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=41", null, m4));
        menuList.add(new Menu("hehe42", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=42", null, m4));
        menuList.add(new Menu("hehe43", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=43", null, m4));
        menuList.add(new Menu("hehe44", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=44", null, m4));
        menuList.add(new Menu("hehe45", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=45", null, m4));
        menuList.add(new Menu("hehe46", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=46", null, m4));


        menuList.add(new Menu("hehe51", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=51", null, m5));
        menuList.add(new Menu("hehe52", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=52", null, m5));
        menuList.add(new Menu("hehe53", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=53", null, m5));
        menuList.add(new Menu("hehe54", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=54", null, m5));
        menuList.add(new Menu("hehe55", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=55", null, m5));
        menuList.add(new Menu("hehe56", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=56", null, m5));


        menuList.add(new Menu("hehe61", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=61", null, m6));
        menuList.add(new Menu("hehe62", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=62", null, m6));
        menuList.add(new Menu("hehe63", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=63", null, m6));
        menuList.add(new Menu("hehe64", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=64", null, m6));
        menuList.add(new Menu("hehe65", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=65", null, m6));
        menuList.add(new Menu("hehe66", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=66", null, m6));

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