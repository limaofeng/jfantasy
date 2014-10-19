package com.fantasy.wx.bean.pojo.group;

import com.fantasy.wx.bean.pojo.group.*;

/**
 * 群发文本消息
 * Created by zzzhong on 2014/7/15.
 */
public class NewGroupMessage extends GroupMessage {
    private New mpnews;
    public class New{
        public String media_id;

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }
    }

    public New getMpnews() {
        return mpnews;
    }

    public void setMpnews(New mpnews) {
        this.mpnews = mpnews;
    }
}
