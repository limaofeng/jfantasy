package com.fantasy.wx.framework.message;

import com.fantasy.wx.framework.message.content.News;

import java.util.List;

/**
 * 图文消息
 */
public class NewsMessage extends AbstractWeiXinMessage<List<News>>{

    public NewsMessage(List<News> content) {
        super(content);
    }

}
