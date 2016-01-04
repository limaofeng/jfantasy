package org.jfantasy.wx.framework.message;

import org.jfantasy.wx.framework.message.content.Video;

import java.util.Date;

/**
 * 微信视频消息
 */
public class VideoMessage extends AbstractWeiXinMessage<Video> {

    public VideoMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

    public VideoMessage(Video content) {
        super(content);
    }

}
