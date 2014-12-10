package com.fantasy.wx.handler;

import com.fantasy.wx.exception.WxException;
import com.fantasy.wx.user.service.impl.UserInfoService;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * Created by zzzhong on 2014/12/9.
 */
@Component
public class FoucsHandler implements WxMpMessageHandler {
    @Resource
    private UserInfoService userInfoService;
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context) {
        try {
            userInfoService.refresh(wxMessage.getToUserName());
        } catch (WxException e) {
            e.printStackTrace();
        }
        WxMpXmlOutTextMessage m = WxMpXmlOutMessage.TEXT()
                .content("欢迎关注")
                .fromUser(wxMessage.getToUserName())
                .toUser(wxMessage.getFromUserName())
                .build();
        return m;
    }
}
