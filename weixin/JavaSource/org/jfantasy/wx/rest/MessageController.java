package org.jfantasy.wx.rest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.wx.framework.core.WeiXinCoreHelper;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionFactory;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
import org.jfantasy.wx.framework.message.WeiXinMessage;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/** 微信消息推送接口 **/
@RestController
@RequestMapping("/weixin/accounts/{appid}/messages")
public class MessageController {

    private final static Log LOG = LogFactory.getLog(MessageController.class);

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    /** 微信消息接口 - 接口接收微信公众平台推送的微信消息及事件,直接调用无效 **/
    @RequestMapping(value = "/push", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String push(@PathVariable("appid") String appid, HttpServletRequest request) throws IOException {
        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            return echostr;
        }
        //解析数据
        //打开session,并保存到上下文
        try {
            WeiXinSession session = WeiXinSessionUtils.saveSession(weiXinSessionFactory.openSession(appid));
            WeiXinCoreHelper helper = weiXinSessionFactory.getWeiXinCoreHelper();
            WeiXinMessage message = helper.parseInMessage(session, request);

            WeiXinMessage returnMessage = weiXinSessionFactory.execute(message);
            if (returnMessage == null) {
                return "";
            }
            String outMessage = helper.buildOutMessage(session, request.getParameter("encrypt_type"), returnMessage);
            if (LOG.isDebugEnabled()) {
                LOG.debug("outMessage=" + outMessage);
            }
            return outMessage;
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            return e.getMessage();
        } finally {
            WeiXinSessionUtils.closeSession();
        }
    }

}
