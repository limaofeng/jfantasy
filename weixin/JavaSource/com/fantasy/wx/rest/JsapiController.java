package com.fantasy.wx.rest;

import com.fantasy.wx.framework.core.Jsapi;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.session.WeiXinSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "weixin-jsapi", description = "微信 JS API")
@RestController
@RequestMapping("/weixin/accounts/{appid}/jsapi")
public class JsapiController {

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    @ApiOperation(value = "获取 jsticket", notes = "获取微信的 jsticket", response = String.class)
    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    @ResponseBody
    @ApiResponse(code = 200, message = "jsticket", response = String.class)
    public String getTicket(@PathVariable("appid") String appid) throws WeiXinException {
        try {
            WeiXinSession session = WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));
            Jsapi jsapi = session.getJsapi();
            if (jsapi == null) {
                throw new WeiXinException(" jsapi is null ");
            }
            return jsapi.getTicket();
        } finally {
            WeiXinSessionUtils.closeSession();
        }

    }

    @ApiOperation(value = "获取 url 签名", notes = "获取 url 微信的 JSAPI 签名")
    @RequestMapping(value = "/signature", method = RequestMethod.GET)
    @ResponseBody
    public Jsapi.Signature signature(@PathVariable("appid") String appid, @RequestParam(value = "url") String url) throws WeiXinException {
        try {
            WeiXinSession session = WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));
            Jsapi jsapi = session.getJsapi();
            if (jsapi == null) {
                throw new WeiXinException(" jsapi is null ");
            }
            return jsapi.signature(url);
        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

}
