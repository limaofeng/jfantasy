package com.fantasy.wx.message.content;

/**
 * 图片消息对象
 */
public class Image {

    private String url;

    private Media media;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    @Override
    public String toString() {
        return "Image{" +
                "url='" + url + '\'' +
                ", media=" + media +
                '}';
    }
}
