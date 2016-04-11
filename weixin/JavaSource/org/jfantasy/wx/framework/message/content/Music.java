package org.jfantasy.wx.framework.message.content;

import org.jfantasy.filestore.FileItem;

/**
 * 音乐消息
 */
public class Music {
    /**
     * 音乐标题
     */
    private String title;
    /**
     * 音乐描述
     */
    private String description;
    /**
     * 音乐链接
     */
    private String url;
    /**
     * 高品质音乐链接，wifi环境优先使用该链接播放音乐
     */
    private String hqUrl;
    /**
     * 缩略图
     */
    private Media thumb;

    public Music(String title, String description, String url, String hqUrl, FileItem thumb) {
        this(url, hqUrl, thumb);
        this.title = title;
        this.description = description;
    }

    public Music(String url, String hqUrl, FileItem thumb) {
        this.url = url;
        this.hqUrl = hqUrl;
        this.thumb = new Media(thumb, Media.Type.thumb);
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHqUrl() {
        return hqUrl;
    }

    public void setHqUrl(String hqUrl) {
        this.hqUrl = hqUrl;
    }

    public Media getThumb() {
        return thumb;
    }

    public void setThumb(Media thumb) {
        this.thumb = thumb;
    }

    @Override
    public String toString() {
        return "Music{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                ", hqUrl='" + hqUrl + '\'' +
                ", thumb=" + thumb +
                '}';
    }
}
