package com.fantasy.wx.message.content;

/**
 * 图文消息
 */
public class News {
    /**
     * 链接消息
     */
    private Link link;
    /**
     * 图片地址
     */
    private String picUrl;

    public News(String picurl, Link link) {
        this.picUrl = picurl;
        this.link = link;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
