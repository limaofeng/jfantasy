package com.fantasy.wx.rest;

import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/weixin/message")
public class MessageController {

    private final static Log LOG = LogFactory.getLog(MessageController.class);

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    @RequestMapping(value = "/{appid}/push")
    public String push(@PathVariable String appid, HttpServletRequest request, HttpServletResponse response) throws IOException {
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
