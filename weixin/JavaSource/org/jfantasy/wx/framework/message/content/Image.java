package org.jfantasy.wx.framework.message.content;

import org.jfantasy.file.FileItem;

/**
 * 图片消息对象
 */
public class Image {

    private String url;

    private Media media;

    public Image(FileItem fileItem) {
        this.media = new Media(fileItem,Media.Type.image);
    }

    public Image(Media media, String url) {
        this.media = media;
        this.media.setType(Media.Type.image);
        this.url = url;
    }

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
