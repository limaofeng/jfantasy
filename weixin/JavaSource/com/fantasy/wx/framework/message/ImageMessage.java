package com.fantasy.wx.framework.message;

import com.fantasy.wx.framework.message.content.Image;

import java.util.Date;

/**
 * 图片消息
 */
public class ImageMessage extends AbstractWeiXinMessage<Image>{

    public ImageMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

}
