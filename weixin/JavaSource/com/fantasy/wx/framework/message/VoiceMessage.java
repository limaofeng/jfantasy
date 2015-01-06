package com.fantasy.wx.framework.message;

import com.fantasy.wx.framework.message.content.Voice;

import java.util.Date;

/**
 * 微信语言消息
 */
public class VoiceMessage extends AbstractWeiXinMessage<Voice> {

    public VoiceMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

}
