package com.fantasy.wx.rest;

import com.fantasy.wx.bean.UserInfo;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.service.UserInfoWeiXinService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Api(value = "weixin-users", description = "微信用户(粉丝)接口")
@RestController("weixin.UserController")
@RequestMapping("/weixin/users")
public class UserController {

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    @ApiOperation(value = "获取微信粉丝", notes = "通过粉丝ID获取关注的用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserInfo view(@PathVariable Long id, HttpServletResponse response) throws WeiXinException, IOException {
        UserInfo userInfo = userInfoWeiXinService.get(id);
        if (userInfo == null) {
            response.sendError(404);
            return null;
        }
        return userInfo;
    }

    @ApiOperation(value = "获取微信粉丝", notes = "通过粉丝ID获取关注的用户信息")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public UserInfo update(@PathVariable Long id, @RequestBody UserInfo userInfo) throws WeiXinException, IOException {
        userInfo.setId(id);
        return userInfoWeiXinService.save(userInfo);
    }

}
