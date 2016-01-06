package org.jfantasy.wx.framework.message;

import org.jfantasy.wx.framework.message.content.Image;

import java.util.Date;

/**
 * 图片消息
 */
public class ImageMessage extends AbstractWeiXinMessage<Image> {

    public ImageMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

    public ImageMessage(Image content) {
        super(content);
    }

}
