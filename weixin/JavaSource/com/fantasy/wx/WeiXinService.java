package com.fantasy.wx;

import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.factory.WeiXinSessionFactory;
import com.fantasy.wx.message.service.IMessageService;
import com.fantasy.wx.session.WeiXinSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信 service
 */
@Service
public class WeiXinService implements InitializingBean {

    private final static Log LOG = LogFactory.getLog(WeiXinService.class);

    private WeiXinSessionFactory weiXinSessionFactory;

    @Resource
    private IMessageService iMessageService;
    @Resource
    public WeixinConfigInit weixinConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (weiXinSessionFactory == null) {
            LOG.error("weiXinSessionFactory is null");
            throw new WeiXinException("weiXinSessionFactory is null");
        }
    }

    public void onEvent(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String appid = "wxcbc2c9fb9d585cd3";

        PrintWriter writer = response.getWriter();
        String echostr = request.getParameter("echostr");

        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            writer.write(echostr);
            return;
        }

        //解析数据
        try {
            WeiXinSession weiXinSession = weiXinSessionFactory.openSession(appid);
            WeiXinCoreHelper helper = weiXinSessionFactory.getWeiXinCoreHelper();
            WeiXinMessage message = helper.parse(weiXinSession,request);
        } catch (WeiXinException e) {
            writer.write(e.getMessage());
        }


//        WxMpXmlOutMessage outMessage = weixinConfig.getWxMpMessageRouter().route(inMessage);
//        if (outMessage != null) {
//            if ("raw".equals(encryptType)) {
//                response.getWriter().write(outMessage.toXml());
//            } else if ("aes".equals(encryptType)) {
//                response.getWriter().write(outMessage.toEncryptedXml(weixinConfig.getWxMpConfigStorage()));
//            }
//            return NONE;
//        }
//        return NONE;
    }

    @Autowired
    public void setWeiXinSessionFactory(WeiXinSessionFactory weiXinSessionFactory) {
        this.weiXinSessionFactory = weiXinSessionFactory;
    }
}
