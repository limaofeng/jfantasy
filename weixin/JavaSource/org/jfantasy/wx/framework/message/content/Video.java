package org.jfantasy.wx.framework.message.content;

import org.jfantasy.file.FileItem;

/**
 * 视频消息
 */
public class Video {

    /**
     * 视频标题
     */
    private String title;
    /**
     * 视频描述
     */
    private String description;
    /**
     * 缩略图
     */
    private Media thumb;
    /**
     * 视频
     */
    private Media media;

    public Video(String title, String description, FileItem media) {
        this.title = title;
        this.description = description;
        this.media = new Media(media, Media.Type.video);
    }

    public Video(String title, String description, FileItem media, FileItem thumb) {
        this.title = title;
        this.description = description;
        this.media = new Media(media, Media.Type.video);
        this.thumb = new Media(thumb, Media.Type.thumb);
    }

    public Video(FileItem media, FileItem thumb) {
        this.media = new Media(media, Media.Type.video);
        this.thumb = new Media(thumb, Media.Type.thumb);
    }

    public Video(Media media, Media thumb) {
        this.media = media;
        this.media.setType(Media.Type.video);
        this.thumb = thumb;
        this.thumb.setType(Media.Type.thumb);
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Video{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", thumb=" + thumb +
                ", media=" + media +
                '}';
    }
}
