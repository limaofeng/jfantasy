package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.wx.menu.bean.WxMenu;
import junit.framework.Assert;
import me.chanjar.weixin.common.api.WxConsts;
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
public class TapasMenuServiceTest {
    private static final Log logger = LogFactory.getLog(WxMenu.class);
    @Resource
    private IMenuService iMenuService;

    @Before
    public void setUp() throws Exception {
        List<WxMenu> wxMenuList = new ArrayList<WxMenu>();
        WxMenu m1 = new WxMenu("扫一扫", WxConsts.BUTTON_VIEW, 0, 1, "", null, null);
        WxMenu m2 = new WxMenu("文化", WxConsts.BUTTON_VIEW, 0, 2, "", null, null);
        WxMenu m3 = new WxMenu("社交", WxConsts.BUTTON_VIEW, 0, 3, "", null, null);
        wxMenuList.add(m1);
        wxMenuList.add(m2);
        wxMenuList.add(m3);

        iMenuService.saveList(wxMenuList);
        wxMenuList = new ArrayList<WxMenu>();

        //
        wxMenuList.add(new WxMenu("扫一扫", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/operate/list?seatCode=TP002", null, m1));

        wxMenuList.add(new WxMenu("什么是Tapas", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/cms/culture/description", null, m2));
        wxMenuList.add(new WxMenu("店老板", WxConsts.BUTTON_VIEW, 1, 2, "http://42.121.56.178:8888/tapas_client/cms/culture/boss", null, m2));
        wxMenuList.add(new WxMenu("西班牙旅游", WxConsts.BUTTON_VIEW, 3, 1, "http://42.121.56.178:8888/tapas_client/cms/culture/trip", null, m2));

        wxMenuList.add(new WxMenu("常客", WxConsts.BUTTON_VIEW, 1, 1, "http://42.121.56.178:8888/tapas_client/cms/social/regulars", null, m3));
        wxMenuList.add(new WxMenu("学习西班牙语", WxConsts.BUTTON_VIEW, 1, 2, "http://42.121.56.178:8888/tapas_client/cms/social/learning", null, m3));

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