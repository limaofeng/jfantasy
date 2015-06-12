package com.fantasy.wx.rest;

import com.fantasy.wx.framework.core.Jsapi;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weixin/jsapi")
public class JsapiController {

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public String getTicket() throws WeiXinException {
        Jsapi jsapi = weiXinSessionFactory.getCurrentSession().getJsapi();
        if (jsapi == null) {
            throw new WeiXinException(" jsapi is null ");
        }
        return jsapi.getTicket();
    }

    @RequestMapping(value = "/signature", method = RequestMethod.GET)
    public Jsapi.Signature signature(String url) throws WeiXinException {
        Jsapi jsapi = weiXinSessionFactory.getCurrentSession().getJsapi();
        if (jsapi == null) {
            throw new WeiXinException(" jsapi is null ");
        }
        return jsapi.signature(url);
    }

}
