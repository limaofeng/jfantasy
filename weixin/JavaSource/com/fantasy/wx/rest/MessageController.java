package com.fantasy.wx.rest;

import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Api(value = "weixin-messages", description = "微信消息推送接口")
@RestController
@RequestMapping("/weixin/accounts/{appid}/messages")
public class MessageController {

    private final static Log LOG = LogFactory.getLog(MessageController.class);

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    @ApiOperation(value = "微信消息接口", notes = "接口接收微信公众平台推送的微信消息及事件,直接调用无效")
    @ApiResponse(code = 200, message = "OK", response = WeiXinMessage.class)
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
