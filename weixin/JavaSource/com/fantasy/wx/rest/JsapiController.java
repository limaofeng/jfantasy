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

    /**
     * @api {get} /weixin/jsapi/ticket  获取 ticket
     * @apiVersion 3.3.4
     * @apiName ticket
     * @apiGroup 微信 JSAPI
     * @apiPermission admin
     * @apiDescription 获取微信的 jsticket
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/jsapi/ticket
     * @apiError NoAccessRight Only authenticated Admins can access the data.
     * @apiError UserNotFound   The <code>id</code> of the User was not found.
     * @apiErrorExample Response (example):
     * HTTP/1.1 401 Not Authenticated
     * {
     * "error": "NoAccessRight"
     * }
     */
    @RequestMapping(value = "/ticket", method = RequestMethod.GET)
    public String getTicket() throws WeiXinException {
        Jsapi jsapi = weiXinSessionFactory.getCurrentSession().getJsapi();
        if (jsapi == null) {
            throw new WeiXinException(" jsapi is null ");
        }
        return jsapi.getTicket();
    }

    /**
     * @api {get} /weixin/jsapi/signature   获取 url 签名
     * @apiVersion 3.3.4
     * @apiName signature
     * @apiGroup 微信 JSAPI
     * @apiPermission admin
     * @apiDescription 获取 url 微信的 JSAPI 签名
     * @apiParam {String} url 生成签名的url
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/jsapi/signature?url=http://www.jfantasy.org
     * @apiSuccess {String}     noncestr            随机字符串
     * @apiSuccess {String}     ticket              ticket
     * @apiSuccess {long}       timestamp           时间戳
     * @apiSuccess {String}     url                 URL
     * @apiSuccess {String}     signature           签名串
     * @apiError NoAccessRight  只有被授权通过,才能获取数据。
     * @apiError WeiXinError    微信端抛出异常
     * @apiErrorExample Response (示例):
     * HTTP/1.1 401 Not Authenticated
     * {
     * "error": "NoAccessRight"
     * }
     */
    @RequestMapping(value = "/signature", method = RequestMethod.GET)
    public Jsapi.Signature signature(String url) throws WeiXinException {
        Jsapi jsapi = weiXinSessionFactory.getCurrentSession().getJsapi();
        if (jsapi == null) {
            throw new WeiXinException(" jsapi is null ");
        }
        return jsapi.signature(url);
    }

}
