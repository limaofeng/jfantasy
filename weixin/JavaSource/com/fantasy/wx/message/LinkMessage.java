package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Link;

import java.util.Date;

/**
 * 链接消息
 */
public class LinkMessage extends AbstractWeiXinMessage<Link> {

    public LinkMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

    @Override
    public Link getContent() {
        return null;
    }

}
