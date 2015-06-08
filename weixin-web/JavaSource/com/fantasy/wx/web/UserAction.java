package com.fantasy.wx.web;

import com.fantasy.framework.struts2.rest.RestActionSupport;
import com.fantasy.framework.util.common.PropertiesHelper;
import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.framework.session.WeiXinSession;
import com.fantasy.wx.service.UserInfoWeiXinService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserAction extends RestActionSupport {

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;
    @Autowired
    private WeiXinSessionFactory factory;

    public String search() {
        return SUCCESS;
    }

    public String create() {
        return SUCCESS;
    }

    public String update(UserInfo user) {
        this.attrs.put(ROOT, userInfoWeiXinService.save(user));
        return SUCCESS;
    }

    public String delete() {
        return SUCCESS;
    }

    public String view(String id) throws WeiXinException {
        WeiXinSession session = factory.openSession(PropertiesHelper.load("props/application.properties").getProperty("weixin.appid"));
        User user = session.getAuthorizedUser(id);
        if(user != null){
            this.attrs.put(ROOT, userInfoWeiXinService.refresh(user.getOpenId()));
        }
        return SUCCESS;
    }

}
