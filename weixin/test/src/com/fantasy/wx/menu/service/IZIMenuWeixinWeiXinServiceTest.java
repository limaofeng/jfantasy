package com.fantasy.wx.menu.service;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.wx.bean.MenuWeixin;
import com.fantasy.wx.framework.message.content.Menu;
import com.fantasy.wx.framework.oauth2.Scope;
import com.fantasy.wx.service.MenuWeiXinService;
import com.fantasy.wx.service.UserInfoWeiXinService;
import junit.framework.Assert;
import me.chanjar.weixin.common.api.WxConsts;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
public class IZIMenuWeixinWeiXinServiceTest {
    private static final Log logger = LogFactory.getLog(MenuWeixin.class);

    @Autowired
    private MenuWeiXinService iMenuWeiXinService;
    @Resource
    private UserInfoWeiXinService userInfoWeiXinService;

    public void setUp() throws Exception {
        List<MenuWeixin> menuWeixinList = new ArrayList<MenuWeixin>();

        MenuWeixin m1 = new MenuWeixin("分类列表", WxConsts.BUTTON_VIEW, 0, 2, null, "", null);
        MenuWeixin m2 = new MenuWeixin("问答", WxConsts.BUTTON_VIEW, 0, 3, null, "", null);
        MenuWeixin m3 = new MenuWeixin("账户", WxConsts.BUTTON_VIEW, 0, 1, "http://112.124.22.92:8080/iziwx/account/info", null, null);
        menuWeixinList.add(m1);
        menuWeixinList.add(m2);
        menuWeixinList.add(m3);

        iMenuWeiXinService.saveList(menuWeixinList);
        menuWeixinList = new ArrayList<MenuWeixin>();

        menuWeixinList.add(new MenuWeixin("商场首页", WxConsts.BUTTON_VIEW, 1, 1, "http://112.124.22.92:8080/iziwx/bazaar/index",null, m1));
        menuWeixinList.add(new MenuWeixin("品牌首页", WxConsts.BUTTON_VIEW, 1, 2, "http://112.124.22.92:8080/iziwx/brand/index", null, m1));
        menuWeixinList.add(new MenuWeixin("搜索", WxConsts.BUTTON_VIEW, 1, 3, "http://112.124.22.92:8080/iziwx/search/index", null, m1));

        menuWeixinList.add(new MenuWeixin("问答首页", WxConsts.BUTTON_VIEW, 1, 1, "http://112.124.22.92:8080/iziwx/question/index", null, m2));
        menuWeixinList.add(new MenuWeixin("添加问题", WxConsts.BUTTON_VIEW, 1, 2, "http://112.124.22.92:8080/iziwx/question/add", null, m2));
        menuWeixinList.add(new MenuWeixin("问题搜索", WxConsts.BUTTON_VIEW, 1, 3, "http://112.124.22.92:8080/iziwx/question/search", null, m2));

        iMenuWeiXinService.saveList(menuWeixinList);
    }

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
    public void testRefresh() throws Exception {
        int r = iMenuWeiXinService.refresh();
        Assert.assertEquals(r, 0);
    }
    @Test
    public void tesTrefresh(){
        Menu menu1=Menu.view("账户信息","http://112.124.22.92:8080/iziwx/account/info");
        Menu[] menus=new Menu[3];
        menus[0]=Menu.view("商场首页","http://112.124.22.92:8080/iziwx/bazaar/index");
        menus[1]=Menu.view("品牌首页","http://112.124.22.92:8080/iziwx/brand/index");
        menus[2]=Menu.view("搜索","http://112.124.22.92:8080/iziwx/search/index");
        Menu menu2=new Menu("分类列表",menus);

        Menu[] menus2=new Menu[3];
        menus2[0]=Menu.view("问答首页","http://112.124.22.92:8080/iziwx/question/index");
        menus2[1]=Menu.view("添加问题","http://112.124.22.92:8080/iziwx/question/add");
        menus2[2]=Menu.view("问题搜索","http://112.124.22.92:8080/iziwx/question/search_info");
        Menu menu3=new Menu("问答",menus2);
        iMenuWeiXinService.refresh(menu1,menu2,menu3);
    }
    @Test
    public void testUrl(){
        String url=iMenuWeiXinService.createOauth2Url("http://semilean.eicp.net/iziwx/account/info", Scope.base);
        logger.debug(url);
    }
}