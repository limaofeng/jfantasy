package com.fantasy.wx.bean.resp;

import com.fantasy.wx.bean.resp.*;

import java.util.List;

/**
 * 文本消息
 * Created by zzzhong on 2014/6/17.
 */
public class NewsMessage extends BaseMessage {
    // 图文消息个数，限制为10条以内
    private int ArticleCount;
    // 多条图文消息信息，默认第一个item为大图
    private List<ArticleMessage> Articles;

    public int getArticleCount() {
        return ArticleCount;
    }

    public void setArticleCount(int articleCount) {
        ArticleCount = articleCount;
    }

    public List<ArticleMessage> getArticles() {
        return Articles;
    }

    public void setArticles(List<ArticleMessage> articles) {
        Articles = articles;
    }
}
