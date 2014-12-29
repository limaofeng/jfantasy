package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Video;

import java.util.Date;

/**
 * 微信视频消息
 */
public class VideoMessage extends AbstractWeiXinMessage<Video> {

    public VideoMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

}
