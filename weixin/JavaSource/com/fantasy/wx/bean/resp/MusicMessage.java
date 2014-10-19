package com.fantasy.wx.bean.resp;

import com.fantasy.wx.bean.resp.*;
import com.fantasy.wx.bean.resp.Music;

/**
 *  音乐消息
 * Created by zzzhong on 2014/6/17.
 */
public class MusicMessage extends BaseMessage {
    // 音乐
    private com.fantasy.wx.bean.resp.Music Music;

    public com.fantasy.wx.bean.resp.Music getMusic() {
        return Music;
    }

    public void setMusic(com.fantasy.wx.bean.resp.Music music) {
        Music = music;
    }
}
