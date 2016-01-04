package org.jfantasy.wx.framework.message;

import org.jfantasy.wx.framework.message.content.Link;

import java.util.Date;

/**
 * 链接消息
 */
public class LinkMessage extends AbstractWeiXinMessage<Link> {

    public LinkMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

}
