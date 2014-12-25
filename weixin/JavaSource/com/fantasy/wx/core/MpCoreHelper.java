package com.fantasy.wx.core;

import com.fantasy.wx.WeiXinCoreHelper;
import com.fantasy.wx.WeiXinMessage;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.message.MessageFactory;
import com.fantasy.wx.session.AccountDetails;
import com.fantasy.wx.session.WeiXinSession;
import me.chanjar.weixin.mp.api.*;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信服务号与订阅号
 */
@Component
public class MpCoreHelper implements WeiXinCoreHelper {

    private Map<String, WeiXinDetails> weiXinDetailsMap = new HashMap<String, WeiXinDetails>();

    @Override
    public void register(AccountDetails accountDetails) {
        if (weiXinDetailsMap.containsKey(accountDetails.getAppId())) {
            weiXinDetailsMap.remove(accountDetails.getAppId());
        }
        weiXinDetailsMap.put(accountDetails.getAppId(), new WeiXinDetails(accountDetails));
    }

    @Override
    public WeiXinMessage parse(WeiXinSession session, HttpServletRequest request) throws WeiXinException {
        String signature = request.getParameter("signature");
        String nonce = request.getParameter("nonce");
        String timestamp = request.getParameter("timestamp");
        String encrypt_type = request.getParameter("encrypt_type");
        String msg_signature = request.getParameter("msg_signature");
        InputStream input;
        try {
            input = request.getInputStream();
        } catch (IOException e) {
            throw new WeiXinException(e.getMessage());
        }

        WeiXinDetails weiXinDetails = getWeiXinDetails(session.getId());

        if (!weiXinDetails.getWxMpService().checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            throw new WeiXinException("非法请求");
        }


        String encryptType = StringUtils.isBlank(encrypt_type) ? "raw" : encrypt_type;
        WxMpXmlMessage inMessage;
        if ("raw".equals(encryptType)) {
            // 明文传输的消息
            inMessage = WxMpXmlMessage.fromXml(input);
        } else if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            inMessage = WxMpXmlMessage.fromEncryptedXml(input, weiXinDetails.getWxMpConfigStorage(), timestamp, nonce, msg_signature);
        } else {
            throw new WeiXinException("不可识别的加密类型");
        }
        if("text".equals(inMessage.getMsgType())){
            MessageFactory.createTextMessage();
        }else if("image".equals(inMessage.getMsgType())){
            MessageFactory.createTextMessage();
        }else if("voice".equals(inMessage.getMsgType())){
            MessageFactory.createTextMessage();
        }else if("video".equals(inMessage.getMsgType())){
            MessageFactory.createTextMessage();
        }else if("location".equals(inMessage.getMsgType())){
            MessageFactory.createTextMessage();
        }else if("link".equals(inMessage.getMsgType())){
            MessageFactory.createTextMessage();
        }

        return null;
    }

    @Override
    public void sendMessage(WeiXinMessage message) {

    }

    public WeiXinDetails getWeiXinDetails(String appid) throws WeiXinException {
        if (!weiXinDetailsMap.containsKey(appid)) {
            throw new WeiXinException("[appid=" + appid + "]未注册！");
        }
        return weiXinDetailsMap.get(appid);
    }

    public static class WeiXinDetails {
        private WxMpService wxMpService;
        private WxMpConfigStorage wxMpConfigStorage;
        private WxMpMessageRouter wxMpMessageRouter;

        public WeiXinDetails(AccountDetails accountDetails) {
            this.wxMpService = new WxMpServiceImpl();

            WxMpInMemoryConfigStorage wxMpConfigStorage = new WxMpInMemoryConfigStorage();
            wxMpConfigStorage.setAppId(accountDetails.getAppId());
            wxMpConfigStorage.setSecret(accountDetails.getSecret());
            wxMpConfigStorage.setToken(accountDetails.getToken());
            wxMpConfigStorage.setAesKey(accountDetails.getAesKey());

            wxMpService.setWxMpConfigStorage(this.wxMpConfigStorage = wxMpConfigStorage);

            this.wxMpMessageRouter = new WxMpMessageRouter(wxMpService);

            wxMpMessageRouter
                    .rule()
                    .async(false)
                    .handler(new WxMpMessageHandler() {
                        @Override
                        public WxMpXmlOutMessage handle(WxMpXmlMessage wxMpXmlMessage, Map<String, Object> map, WxMpService wxMpService) {
                            return null;
                        }
                    }).end();
        }

        public WxMpService getWxMpService() {
            return wxMpService;
        }

        public void setWxMpService(WxMpService wxMpService) {
            this.wxMpService = wxMpService;
        }

        public WxMpConfigStorage getWxMpConfigStorage() {
            return wxMpConfigStorage;
        }

        public void setWxMpConfigStorage(WxMpConfigStorage wxMpConfigStorage) {
            this.wxMpConfigStorage = wxMpConfigStorage;
        }
    }

}
