package com.fantasy.wx.bean.req;

import com.fantasy.wx.bean.req.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 文本消息
 * Created by zzzhong on 2014/6/17.
 */
@Entity
@Table(name = "WX_MESSAGE_TEXT")
public class TextMessage extends BaseMessage {
    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    // 消息内容
    @Column(name = "CONTENT",length=5000)
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
