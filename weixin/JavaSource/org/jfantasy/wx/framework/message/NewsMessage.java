package org.jfantasy.wx.framework.message;

import org.jfantasy.wx.framework.message.content.News;

import java.util.List;

/**
 * 图文消息
 */
public class NewsMessage extends AbstractWeiXinMessage<List<News>>{

    public NewsMessage(List<News> content) {
        super(content);
    }

}
