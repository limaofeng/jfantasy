package com.fantasy.wx.bean.req;

import com.fantasy.wx.bean.req.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 链接消息
 * Created by zzzhong on 2014/6/17.
 */
@Entity
@Table(name = "WX_MESSAGE_LINK")
public class LinkMessage extends BaseMessage {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    // 消息标题
    @Column(name = "TITLE",length=500)
    private String Title;
    // 消息描述
    @Column(name = "DESCRIPTION",length=2000)
    private String Description;
    // 消息链接
    @Column(name = "URL",length=500)
    private String Url;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }
}
