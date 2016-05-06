package org.jfantasy.wx.bean;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.jackson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzzhong on 2014/12/16.
 */
@Entity
@Table(name = "WX_GROUP_NEWS")
@JsonFilter(JSON.CUSTOM_FILTER)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class GroupNews   extends BaseBusEntity {
    //自动保存的字段------------------------------------start
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    // 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb），次数为news，即图文消息
    @Column(name = "TYPE")
    private String type;
    //媒体文件/图文消息上传后获取的唯一标识
    @Column(name = "MEDIA_ID")
    private String mediaId;
    //媒体文件上传时间
    @Column(name = "CREATED_AT")
    private long createdAt;

    //自动保存的字段------------------------------------end

    //图文列表
    @OneToMany(mappedBy = "news", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    private List<GroupNewsArticle> articles = new ArrayList<GroupNewsArticle>();

    public List<GroupNewsArticle> getArticles() {
        return articles;
    }

    public void setArticles(List<GroupNewsArticle> articles) {
        this.articles = articles;
    }
    public void addArticle(GroupNewsArticle article){
        articles.add(article);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
