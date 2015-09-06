package com.fantasy.wx.rest;

import com.fantasy.wx.framework.core.WeiXinCoreHelper;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionFactory;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.WeiXinMessage;
import com.fantasy.wx.framework.session.WeiXinSession;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @apiDefine WeiXinMessage
 * @apiSuccess (Success 200)    {Long}      Id              MsgId	消息id，64位整型
 * @apiSuccess (Success 200)    {String}    fromUserName    发送方帐号（OpenID或者微信公众号的原始ID）
 * @apiSuccess (Success 200)    {Date}      createTime      消息创建时间
 * @apiSuccess (Success 200)    {Content}   content         消息内容(参考：WeiXinMessage 接口与其实现类)
 * @apiSuccess (Success 200)    {String}    toUserName      消息的接收方(OpenID或者微信公众号的原始ID)
 * @apiVersion 3.3.8
 */
@Api(value = "weixin-messages", description = "微信消息推送接口")
@RestController
@RequestMapping("/weixin/messages")
public class MessageController {

    private final static Log LOG = LogFactory.getLog(MessageController.class);

    @Autowired
    private WeiXinSessionFactory weiXinSessionFactory;

    /**
     * @api {get} /weixin/message/:appid/push   微信消息接口
     * @apiVersion 3.3.8
     * @apiName push
     * @apiGroup 微信消息
     * @apiDescription 该接口为微信公众平台中的接口配置地址。
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/message/wx0e7cef7ad73417eb/push
     * @apiUse WeiXinMessage
     */
    @ApiOperation(value = "微信消息接口", notes = "接口接收微信公众平台推送的微信消息及事件,直接调用无效")
    @RequestMapping(value = "/{appid}/push", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String push(@PathVariable String appid, HttpServletRequest request) throws IOException {
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
