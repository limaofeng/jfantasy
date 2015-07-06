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

/**
 * @apiDefine UserInfo
 * @apiSuccess {String}     id
 * @apiSuccess {String}     openId              标识
 * @apiSuccess {long}       nickname            昵称
 * @apiSuccess {String}     sex                 性别
 * @apiSuccess {String}     country             所在国家
 * @apiSuccess {String}     province            所在省份
 * @apiSuccess {String}     city                所在城市
 * @apiSuccess {String}     avatar              头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），没有头像时该项为空
 * @apiSuccess {String}     subscribeTime       关注时间，为时间戳。如果曾多次关注，则取最后关注时间
 * @apiSuccess {String}     subscribe           是否订阅该公众号标识，值为0时，代表此没有关注该公众号，拉取不到其余信息。
 * @apiVersion 3.3.4
 */
@RestController("weixin.UserController")
@RequestMapping("/weixin/user")
public class UserController {

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    /**
     * @api {get} /weixin/user/:openid  获取粉丝
     * @apiVersion 3.3.4
     * @apiName getUser
     * @apiGroup 微信粉丝
     * @apiPermission admin
     * @apiDescription 通过粉丝ID获取关注的用户信息
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/user/oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw
     * @apiError WeiXinError    微信端抛出异常,对应 response status <code>403</code>
     * @apiUse UserInfo
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{openid}", method = RequestMethod.GET)
    public UserInfo getUser(@PathVariable String openid, HttpServletResponse response) throws WeiXinException, IOException {
        UserInfo userInfo = userInfoWeiXinService.checkCreateMember(openid);
        if (userInfo == null) {
            response.sendError(404);
            return null;
        }
        return userInfo;
    }

    /**
     * @api {get} /weixin/user/oauth2/:code  通过安全code获取粉丝
     * @apiVersion 3.3.4
     * @apiName getUserByOauth2Code
     * @apiGroup 微信粉丝
     * @apiPermission admin
     * @apiDescription 通过 oauth2 code 获取关注的用户信息
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/user/oauth2/oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw
     * @apiUse UserInfo
     * @apiError WeiXinError    微信端抛出异常,对应 response status <code>403</code>
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/oauth2/{code}", method = RequestMethod.GET)
    public UserInfo getUserByAuthorizedCode(@PathVariable String code, HttpServletResponse response) throws WeiXinException, IOException {
        User user = WeiXinSessionUtils.getCurrentSession().getOauth2User(code);
        if (user == null) {
            response.sendError(404);
            return null;
        }
        return getUser(user.getOpenId(), response);
    }

}
