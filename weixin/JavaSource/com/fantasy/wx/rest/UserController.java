package com.fantasy.wx.rest;

import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.service.UserInfoWeiXinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController("weixin.UserController")
@RequestMapping("/weixin/user")
public class UserController {

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    @RequestMapping(value = "/{openid}", method = RequestMethod.GET)
    public UserInfo getUser(@PathVariable String openid) throws WeiXinException, IOException {
        return userInfoWeiXinService.checkCreateMember(openid);
    }

    @RequestMapping(value = "/authorized/{code}", method = RequestMethod.GET)
    public UserInfo getUserByAuthorizedCode(@PathVariable String code, HttpServletResponse response) throws WeiXinException, IOException {
        User user = WeiXinSessionUtils.getCurrentSession().getAuthorizedUser(code);
        if (user == null) {
            response.sendError(404);
            return null;
        }
        return getUser(user.getOpenId());
    }

}
