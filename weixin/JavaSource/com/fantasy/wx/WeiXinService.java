package com.fantasy.wx;

import com.fantasy.wx.account.init.WeixinConfigInit;
import com.fantasy.wx.core.WeiXinCoreHelper;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.factory.WeiXinSessionFactory;
import com.fantasy.wx.message.WeiXinMessage;
import com.fantasy.wx.message.service.IMessageService;
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

        //http://xxx/appid/msg
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
            //打开session,并保存到上下文
            weiXinSessionFactory.openSession(appid);
            WeiXinCoreHelper helper = weiXinSessionFactory.getWeiXinCoreHelper();
            WeiXinMessage message = helper.parseInMessage(request);

            WeiXinMessage returnMessage = weiXinSessionFactory.execute(message);

            if (returnMessage != null) {
                String outMessage = helper.buildOutMessage(request.getParameter("encrypt_type"), returnMessage);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("outMessage=" + outMessage);
                }
                writer.write(outMessage);
            }
        } catch (WeiXinException e) {
            LOG.error(e.getMessage(),e);
            writer.write(e.getMessage());
        }
    }

    @Autowired
    public void setWeiXinSessionFactory(WeiXinSessionFactory weiXinSessionFactory) {
        this.weiXinSessionFactory = weiXinSessionFactory;
    }
}
