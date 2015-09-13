package com.fantasy.wx.rest;

import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.user.User;
import com.fantasy.wx.service.UserInfoWeiXinService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "weixin-accounts-users", description = "微信公众号粉丝管理接口")
@RestController("weixin.accountUserController")
@RequestMapping("/weixin/accounts")
public class AccountUserController {

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;
    @Autowired(required = false)
    private WeiXinSessionFactory weiXinSessionFactory;

    @ApiOperation(value = "获取微信粉丝", notes = "通过粉丝ID获取关注的用户信息")
    @RequestMapping(value = "/{appid}/users/{openid}", method = RequestMethod.GET)
    @ResponseBody
    public UserInfo view(@PathVariable("appid") String appid, @PathVariable String openid, HttpServletResponse response) throws WeiXinException, IOException {
        try {
            WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));

            UserInfo userInfo = userInfoWeiXinService.checkCreateMember(openid);
            if (userInfo == null) {
                response.sendError(404);
                return null;
            }
            return userInfo;

        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

    @ApiOperation(value = "通过 oauth2 code 获取微信粉丝", notes = "通过 oauth2 code 获取关注的用户信息")
    @RequestMapping(value = "/{appid}/users/oauth2/{code}", method = RequestMethod.GET)
    public UserInfo getUserByAuthorizedCode(@PathVariable("appid") String appid, @PathVariable String code, HttpServletResponse response) throws WeiXinException, IOException {
        try {
            WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));

            User user = WeiXinSessionUtils.getCurrentSession().getOauth2User(code);
            if (user == null) {
                response.sendError(404);
                return null;
            }
            return view(appid, user.getOpenId(), response);

        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

}
