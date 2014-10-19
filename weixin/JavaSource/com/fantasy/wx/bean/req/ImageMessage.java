package com.fantasy.wx.bean.req;

import com.fantasy.wx.bean.req.*;

/**
 * 图片消息
 * Created by zzzhong on 2014/6/17.
 */
public class ImageMessage extends BaseMessage {
    // 图片链接
    private String PicUrl;

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }
}
