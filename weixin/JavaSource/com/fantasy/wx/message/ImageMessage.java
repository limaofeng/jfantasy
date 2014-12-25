package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Image;

/**
 * 图片消息
 */
public class ImageMessage extends AbstractWeiXinMessage<Image>{

    @Override
    public Image getContent() {
        return null;
    }

}
