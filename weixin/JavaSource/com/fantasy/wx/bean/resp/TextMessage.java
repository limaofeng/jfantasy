package com.fantasy.wx.bean.resp;

import com.fantasy.wx.bean.resp.*;

/**
 * Created by zzzhong on 2014/6/17.
 */
public class TextMessage extends BaseMessage {
    // 回复的消息内容
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
