package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Media;

/**
 * 微信视频消息
 */
public class VideoMessage extends AbstractWeiXinMessage<Media> {

    @Override
    public Media getContent() {
        return null;
    }

}
