package org.jfantasy.wx.rest;

import org.jfantasy.wx.framework.core.Jsapi;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionFactory;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weixin/accounts/{appid}/jsapi")
public class JsapiController {

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    /**
     * 获取 jsticket
     * @param appid
     * @return
     * @throws WeiXinException
     */
    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    @ResponseBody
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

    /**
     * 获取 url 签名
     * @param appid
     * @param url
     * @return
     * @throws WeiXinException
     */
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
