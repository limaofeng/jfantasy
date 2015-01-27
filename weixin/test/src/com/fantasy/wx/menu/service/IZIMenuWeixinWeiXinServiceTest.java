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
        Menu[] menus0=new Menu[3];
        menus0[0]=Menu.view("商场","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Fbazaar%2Findex&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        menus0[1]=Menu.view("品牌","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Fbrand%2Findex&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        menus0[2]=Menu.view("搜索","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Fsearch%2Findex&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        Menu menu1=new Menu("数据库",menus0);
        Menu[] menus1=new Menu[4];
        menus1[0]=Menu.view("热门问题","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Fquestion%2Findex&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        menus1[1]=Menu.view("我来问","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Fquestion%2Fadd&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        menus1[2]=Menu.click("等我答","dengwoda");
        menus1[3]=Menu.view("搜答案","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Fquestion%2Fsearch_info&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        Menu menu2=new Menu("问答",menus1);
        Menu[] menus2=new Menu[4];
        menus2[0]=Menu.view("用户中心","https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0e7cef7ad73417eb&redirect_uri=http%3A%2F%2Fsemilean.eicp.net%2Fweb%2Faccount%2Finfo&response_type=code&scope=snsapi_userinfo&connect_redirect=1#wechat_redirect");
        menus2[1]=Menu.click("收藏","shoucang");
        menus2[2]=Menu.click("私信", "sixin");
        menus2[3]=Menu.click("提问", "tiwen");
        Menu menu3=new Menu("我的",menus2);
        iMenuWeiXinService.refresh(menu1,menu2,menu3);
    }
    @Test
    public void testUrl(){
        String url=iMenuWeiXinService.createOauth2Url("http://semilean.eicp.net:8080/izi/account/info", Scope.userinfo);
        logger.debug(url);
    }
}