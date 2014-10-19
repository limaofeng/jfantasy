package com.fantasy.wx.bean.pojo.group;

import com.fantasy.wx.bean.pojo.group.*;

/**
 * 群发文本消息
 * Created by zzzhong on 2014/7/15.
 */
public class TextGroupMessage extends GroupMessage {
    private Text text;
    public class Text{
        public String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
