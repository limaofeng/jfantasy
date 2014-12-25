package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Media;

/**
 * 微信语言消息
 */
public class VoiceMessage extends AbstractWeiXinMessage<Media> {
    @Override
    public Media getContent() {
        return null;
    }
}
