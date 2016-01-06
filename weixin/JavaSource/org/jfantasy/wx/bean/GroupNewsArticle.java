package org.jfantasy.wx.bean;

import org.jfantasy.file.bean.FileDetail;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by zzzhong on 2014/12/16.
 */
@Entity
@Table(name = "WX_GROUP_NEWS_ARTICLE")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupNewsArticle {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "NEWS_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_WX_GROUP_NEWS_ARTICLE"))
    private GroupNews news;

    /**
     * (必填) 缩略图文件 生成图文消息缩略图 生成thumbMediaId字段
     */
    @Transient
    private FileDetail thumbFile;
    /**
     * 这个字段由业务方法通过thumbFile 生成图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
     */
    @Column(name = "THUMB_MEDIA_ID")
    private String thumbMediaId;
    /**
     * 图文消息的作者
     */
    @Column(name = "AUTHOR")
    private String author;
    /**
     * (必填) 图文消息的标题
     */
    @Column(name = "TITLE")
    private String title;
    /**
     * 在图文消息页面点击“阅读原文”后的页面链接
     */
    @Column(name = "CONTENT_SOURCE_URL")
    private String contentSourceUrl;
    /**
     * (必填) 图文消息页面的内容，支持HTML标签
     */
    @Column(name = "CONTENT")
    private String content;
    /**
     * 图文消息的描述
     */
    @Column(name = "DIGEST")
    private String digest;
    /**
     * 是否显示封面，true为显示，false为不显示
     */
    @Column(name = "SHOW_COVER_PIC")
    private boolean showCoverPic=true;

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentSourceUrl() {
        return contentSourceUrl;
    }

    public void setContentSourceUrl(String contentSourceUrl) {
        this.contentSourceUrl = contentSourceUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public boolean isShowCoverPic() {
        return showCoverPic;
    }

    public void setShowCoverPic(boolean showCoverPic) {
        this.showCoverPic = showCoverPic;
    }

    public GroupNews getNews() {
        return news;
    }

    public void setNews(GroupNews news) {
        this.news = news;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public FileDetail getThumbFile() {
        return thumbFile;
    }

    public void setThumbFile(FileDetail thumbFile) {
        this.thumbFile = thumbFile;
    }
}
