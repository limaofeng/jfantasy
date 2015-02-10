package com.fantasy.wx.framework.message;

import java.util.Date;

/**
 * 微信文本消息
 */
public class TextMessage extends AbstractWeiXinMessage<String> {

    public TextMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

    public TextMessage(String content) {
        super(content);
    }
}
