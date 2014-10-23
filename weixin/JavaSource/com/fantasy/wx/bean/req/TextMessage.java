package com.fantasy.wx.bean.req;

import com.fantasy.wx.bean.req.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 文本消息
 * Created by zzzhong on 2014/6/17.
 */
public class TextMessage extends BaseMessage {
    // 消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
