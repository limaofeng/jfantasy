package com.fantasy.wx.message.content;

/**
 * 视频消息
 */
public class Video {
    /**
     * 缩略图
     */
    private Media thumb;
    /**
     * 视频
     */
    private Media media;

    public Media getThumb() {
        return thumb;
    }

    public void setThumb(Media thumb) {
        this.thumb = thumb;
    }

    public Media getMedia() {
        return media;
    }

    public void setMedia(Media media) {
        this.media = media;
    }
}
