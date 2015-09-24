package com.fantasy.wx.rest;

import com.fantasy.framework.spring.mvc.error.NotFoundException;
import com.fantasy.wx.bean.User;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "weixin-accounts-users", description = "微信公众号粉丝管理接口")
@RestController("weixin.accountUserController")
@RequestMapping("/weixin/accounts/{appid}/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private WeiXinSessionFactory weiXinSessionFactory;

    @ApiOperation(value = "获取微信粉丝", notes = "通过粉丝ID获取关注的用户信息")
    @RequestMapping(value = "/{openid}", method = RequestMethod.GET)
    @ResponseBody
    public User view(@PathVariable("appid") String appid, @PathVariable String openid, HttpServletResponse response) throws WeiXinException, IOException {
        try {
            WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));

            User user = userService.checkCreateMember(openid);
            if (user == null) {
                throw new NotFoundException("未找到对应的粉丝信息");
            }
            return user;

        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

    @ApiOperation(value = "通过 oauth2 code 获取微信粉丝", notes = "通过 oauth2 code 获取关注的用户信息")
    @RequestMapping(value = "/{code}/oauth2", method = RequestMethod.GET)
    public User getUserByAuthorizedCode(@PathVariable("appid") String appid, @PathVariable("code") String code, HttpServletResponse response) throws WeiXinException, IOException {
        try {
            WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));

            com.fantasy.wx.framework.message.user.User user = WeiXinSessionUtils.getCurrentSession().getOauth2User(code);
            if (user == null) {
                throw new NotFoundException("未找到对应的粉丝信息");
            }
            return view(appid, user.getOpenId(), response);

        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

    @ApiOperation(value = "获取微信粉丝", notes = "通过粉丝ID获取关注的用户信息")
    @RequestMapping(value = "/{openid}", method = RequestMethod.PUT)
    @ResponseBody
    public User update(@PathVariable("appid") String appid, @PathVariable String openid, @RequestBody User user) throws WeiXinException, IOException {
        user.setAppId(appid);
        user.setOpenId(openid);
        return userService.save(user);
    }

}
