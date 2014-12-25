package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.bean.WxMenu;
import com.fantasy.wx.service.MenuService;
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
public class WxWxMenuServiceTest {
    private static final Log logger = LogFactory.getLog(WxMenu.class);

    @Autowired
    private MenuService iMenuService;

    @Before
    public void setUp() throws Exception {
        List<WxMenu> wxMenuList = new ArrayList<WxMenu>();
        WxMenu m1 = new WxMenu("第一个", WxConsts.BUTTON_CLICK, 0, 1, null, "V1001_TODAY_MUSIC", null);
        WxMenu m2 = new WxMenu("第二个", WxConsts.BUTTON_VIEW, 0, 2, "http://www.baidu.com?id=21", "V1001_TODAY_MUSIC", null);
        WxMenu m3 = new WxMenu("第三个", WxConsts.BUTTON_VIEW, 0, 3, "http://www.baidu.com?id=21", null, null);
        WxMenu m4 = new WxMenu("第四个", WxConsts.BUTTON_CLICK, 0, 4, null, "V1001_TODAY_MUSIC", null);
        WxMenu m5 = new WxMenu("第五个", WxConsts.BUTTON_CLICK, 0, 5, null, "V1001_TODAY_MUSIC", null);
        WxMenu m6 = new WxMenu("第六个", WxConsts.BUTTON_CLICK, 0, 6, null, "V1001_TODAY_MUSIC", null);
        wxMenuList.add(m1);
        wxMenuList.add(m2);
        wxMenuList.add(m3);
        wxMenuList.add(m4);
        wxMenuList.add(m5);
        wxMenuList.add(m6);

        iMenuService.saveList(wxMenuList);
        wxMenuList = new ArrayList<WxMenu>();

        wxMenuList.add(new WxMenu("hehe1", WxConsts.BUTTON_CLICK, 1, 1, null, "V1001_GOOD", m1));
        wxMenuList.add(new WxMenu("hehe2", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=2", null, m1));
        wxMenuList.add(new WxMenu("hehe3", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=3", null, m1));
        wxMenuList.add(new WxMenu("hehe4", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=4", null, m1));
        wxMenuList.add(new WxMenu("hehe6", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=5", null, m1));
        wxMenuList.add(new WxMenu("hehe5", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=6", null, m1));


        wxMenuList.add(new WxMenu("hehe21", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=21", null, m2));
        wxMenuList.add(new WxMenu("hehe22", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=22", null, m2));
        wxMenuList.add(new WxMenu("hehe23", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=23", null, m2));
        wxMenuList.add(new WxMenu("hehe24", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=24", null, m2));
        wxMenuList.add(new WxMenu("hehe25", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=25", null, m2));
        wxMenuList.add(new WxMenu("hehe26", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=26", null, m2));


        wxMenuList.add(new WxMenu("hehe36", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=31", null, m3));
        wxMenuList.add(new WxMenu("hehe32", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=32", null, m3));
        wxMenuList.add(new WxMenu("hehe35", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=33", null, m3));
        wxMenuList.add(new WxMenu("hehe34", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=34", null, m3));
        wxMenuList.add(new WxMenu("hehe33", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=35", null, m3));
        wxMenuList.add(new WxMenu("hehe31", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=36", null, m3));


        wxMenuList.add(new WxMenu("hehe41", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=41", null, m4));
        wxMenuList.add(new WxMenu("hehe42", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=42", null, m4));
        wxMenuList.add(new WxMenu("hehe43", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=43", null, m4));
        wxMenuList.add(new WxMenu("hehe44", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=44", null, m4));
        wxMenuList.add(new WxMenu("hehe45", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=45", null, m4));
        wxMenuList.add(new WxMenu("hehe46", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=46", null, m4));


        wxMenuList.add(new WxMenu("hehe51", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=51", null, m5));
        wxMenuList.add(new WxMenu("hehe52", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=52", null, m5));
        wxMenuList.add(new WxMenu("hehe53", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=53", null, m5));
        wxMenuList.add(new WxMenu("hehe54", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=54", null, m5));
        wxMenuList.add(new WxMenu("hehe55", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=55", null, m5));
        wxMenuList.add(new WxMenu("hehe56", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=56", null, m5));


        wxMenuList.add(new WxMenu("hehe61", WxConsts.BUTTON_VIEW, 1, 1, "http://www.baidu.com?id=61", null, m6));
        wxMenuList.add(new WxMenu("hehe62", WxConsts.BUTTON_VIEW, 1, 2, "http://www.baidu.com?id=62", null, m6));
        wxMenuList.add(new WxMenu("hehe63", WxConsts.BUTTON_VIEW, 1, 3, "http://www.baidu.com?id=63", null, m6));
        wxMenuList.add(new WxMenu("hehe64", WxConsts.BUTTON_VIEW, 1, 4, "http://www.baidu.com?id=64", null, m6));
        wxMenuList.add(new WxMenu("hehe65", WxConsts.BUTTON_VIEW, 1, 5, "http://www.baidu.com?id=65", null, m6));
        wxMenuList.add(new WxMenu("hehe66", WxConsts.BUTTON_VIEW, 1, 6, "http://www.baidu.com?id=66", null, m6));

        iMenuService.saveList(wxMenuList);
    }

    @After
    public void tearDown() throws Exception {
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("NOTNULL_parent.id"));
        List<WxMenu> wxMenus = iMenuService.findAll(list);
        Long[] ids = new Long[wxMenus.size()];
        for (int i = 0; i < wxMenus.size(); i++) {
            ids[i] = wxMenus.get(i).getId();
        }
        iMenuService.delete(ids);
        wxMenus = iMenuService.findAll(new ArrayList<PropertyFilter>());
        Long[] bids = new Long[wxMenus.size()];
        for (int i = 0; i < wxMenus.size(); i++) {
            bids[i] = wxMenus.get(i).getId();
        }
        iMenuService.delete(bids);
    }


    @Test
    public void testFindPager() throws Exception {
        Pager<WxMenu> pager = new Pager<WxMenu>();
        List<PropertyFilter> list = new ArrayList<PropertyFilter>();
        list.add(new PropertyFilter("EQI_layer", "0"));
        pager.setOrderBy("sort");
        pager.setOrder(Pager.Order.asc);

        Pager p = iMenuService.findPager(pager, list);
        Assert.assertNotNull(p.getPageItems());
        logger.debug(JSON.serialize(p));
    }

    @Test
    public void testGet() throws Exception {
        List<WxMenu> wxMenus = iMenuService.findAll(new ArrayList<PropertyFilter>());
        WxMenu m = iMenuService.get(wxMenus.get(0).getId());
        Assert.assertNotNull(m);
        logger.debug(m);
    }

    @Test
    public void testRefresh() throws Exception {
        int r = iMenuService.refresh();
        Assert.assertEquals(r, 0);
    }

    @Test
    public void testDeleteMenu() throws Exception {
        int r = iMenuService.deleteMenu();
        Assert.assertEquals(r, 0);
    }
}