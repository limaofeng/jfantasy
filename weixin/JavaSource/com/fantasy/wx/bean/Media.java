package com.fantasy.wx.bean;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 微信媒体文件
 * Created by zzzhong on 2014/6/18.
 */
@Entity
@Table(name = "WX_MEDIA")
public class Media {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    //文件类型
    @Column(name = "TYPE")
    private String type;
    ///微信媒体接口id
    @Column(name = "MEDIA_ID")
    private String mediaId;
    //暂时没用 但是sdk有所以增加字段保存
    @Column(name = "THUMB_MEDIA_ID")
    private String thumbMediaId;
    //上传时间戳
    @Column(name = "CREATED_AT")
    private long createdAt;

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

    public String getThumbMediaId() {
        return thumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        this.thumbMediaId = thumbMediaId;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
