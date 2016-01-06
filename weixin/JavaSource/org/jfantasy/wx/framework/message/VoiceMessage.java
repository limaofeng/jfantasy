package org.jfantasy.wx.framework.message;

import org.jfantasy.wx.framework.message.content.Voice;

import java.util.Date;

/**
 * 微信语言消息
 */
public class VoiceMessage extends AbstractWeiXinMessage<Voice> {

    public VoiceMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

    public VoiceMessage(Voice content) {
        super(content);
    }

}
