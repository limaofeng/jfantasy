package org.jfantasy.wx.rest;

import org.jfantasy.framework.spring.mvc.error.NotFoundException;
import org.jfantasy.wx.bean.User;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionFactory;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
import org.jfantasy.wx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 微信公众号粉丝管理接口 **/
@RestController("weixin.accountUserController")
@RequestMapping("/weixin/accounts/{appid}/users")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired(required = false)
    private WeiXinSessionFactory weiXinSessionFactory;

    /** 获取微信粉丝 - 通过粉丝ID获取关注的用户信息 **/
    @RequestMapping(value = "/{openid}", method = RequestMethod.GET)
    @ResponseBody
    public User view(@PathVariable("appid") String appid, @PathVariable String openid, HttpServletResponse response) throws WeiXinException, IOException {
        try {
            WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));

            User user = userService.checkCreateMember(appid,openid);
            if (user == null) {
                throw new NotFoundException("未找到对应的粉丝信息");
            }
            return user;

        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

    /**
     * 通过 oauth2 code 获取微信粉丝
     * @param appid
     * @param code
     * @param response
     * @return
     * @throws WeiXinException
     * @throws IOException
     */
    @RequestMapping(value = "/{code}/oauth2", method = RequestMethod.GET)
    public User getUserByAuthorizedCode(@PathVariable("appid") String appid, @PathVariable("code") String code, HttpServletResponse response) throws WeiXinException, IOException {
        try {
            WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));

            org.jfantasy.wx.framework.message.user.User user = WeiXinSessionUtils.getCurrentSession().getOauth2User(code);
            if (user == null) {
                throw new NotFoundException("未找到对应的粉丝信息");
            }
            return view(appid, user.getOpenId(), response);

        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

    /** 获取微信粉丝 - 通过粉丝ID获取关注的用户信息 **/
    @RequestMapping(value = "/{openid}", method = RequestMethod.PATCH)
    @ResponseBody
    public User update(@PathVariable("appid") String appid, @PathVariable String openid, @RequestBody User user) throws WeiXinException, IOException {
        user.setAppId(appid);
        user.setOpenId(openid);
        return userService.save(user);
    }

}
