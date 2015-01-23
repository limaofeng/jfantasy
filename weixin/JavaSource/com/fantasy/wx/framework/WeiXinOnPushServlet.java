package com.fantasy.wx.framework;

import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 微信servlet
 */
@Component("weiXinOnPushServlet")
public class WeiXinOnPushServlet extends HttpServlet {

    private final static Log LOG = LogFactory.getLog(WeiXinOnPushServlet.class);

    @Resource
    private WeiXinSessionFactory weiXinSessionFactory;

    private String defaultAppId = "wxcbc2c9fb9d585cd3";

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        //http://xxx/appid/msg
        String appid = defaultAppId;

        PrintWriter writer = response.getWriter();
        String echostr = request.getParameter("echostr");

        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            writer.write(echostr);
            return;
        }

        //解析数据
        try {
            //打开session,并保存到上下文
            WeiXinSession session = weiXinSessionFactory.openSession(appid);
            WeiXinCoreHelper helper = weiXinSessionFactory.getWeiXinCoreHelper();
            WeiXinMessage message = helper.parseInMessage(session, request);

            WeiXinMessage returnMessage = weiXinSessionFactory.execute(message);

            if (returnMessage != null) {
                String outMessage = helper.buildOutMessage(session, request.getParameter("encrypt_type"), returnMessage);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("outMessage=" + outMessage);
                }
                writer.write(outMessage);
            }
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(), e);
            writer.write(e.getMessage());
        }
    }

    public void setDefaultAppId(String defaultAppId) {
        this.defaultAppId = defaultAppId;
    }

}
