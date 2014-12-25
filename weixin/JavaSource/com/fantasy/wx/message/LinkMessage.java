package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Link;

/**
 * 链接消息
 */
public class LinkMessage extends AbstractWeiXinMessage<Link> {

    @Override
    public Link getContent() {
        return null;
    }

}
