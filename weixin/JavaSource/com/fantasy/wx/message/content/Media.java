package com.fantasy.wx.message.content;

/**
 * 微信媒体消息
 */
public class Media {

    private String id;

    public Media(String id) {
        this.id = id;
    }

    private String format;

    public Media(String id, String format) {
        this.id = id;
        this.format = format;
    }

    public String getId() {
        return id;
    }

    public String getFormat() {
        return format;
    }

    @Override
    public String toString() {
        return "Media{" +
                "id='" + id + '\'' +
                ", format='" + format + '\'' +
                '}';
    }
}
