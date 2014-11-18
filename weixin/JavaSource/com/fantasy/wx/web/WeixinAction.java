package com.fantasy.wx.web;

import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.wx.config.init.WeixinConfigInit;
import com.fantasy.wx.message.bean.Message;
import com.fantasy.wx.message.service.MessageService;
import com.fantasy.wx.user.bean.UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zzzhong on 2014/9/23.
 */
public class WeixinAction   extends ActionSupport{

    @Resource
    public WxMpService wxMpService;
    @Resource
    private MessageService messageService;
    @Resource
    public WeixinConfigInit weixinConfig;

    public String operationUrl()throws IOException{
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            response.getWriter().println("非法请求");
            return NONE;
        }

        String echostr = request.getParameter("echostr");
        if (StringUtils.isNotBlank(echostr)) {
            // 说明是一个仅仅用来验证的请求，回显echostr
            response.getWriter().println(echostr);
            return NONE;
        }

        String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
                "raw" :
                request.getParameter("encrypt_type");

        WxMpXmlMessage inMessage = null;
        //BeanUtil.copyProperties();
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
        } else if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            String msgSignature = request.getParameter("msg_signature");
            inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), weixinConfig.getWxMpConfigStorage(), timestamp, nonce, msgSignature);
        } else {
            response.getWriter().println("不可识别的加密类型");
            return NONE;
        }
        Message message=BeanUtil.copyProperties(new Message(), inMessage, null);
        messageService.save(message);

        WxMpXmlOutMessage outMessage = weixinConfig.getWxMpMessageRouter().route(inMessage);
        if (outMessage != null) {
            if ("raw".equals(encryptType)) {
                response.getWriter().write(outMessage.toXml());
            } else if ("aes".equals(encryptType)) {
                response.getWriter().write(outMessage.toEncryptedXml(weixinConfig.getWxMpConfigStorage()));
            }
            return NONE;
        }
        return NONE;
    }
}
