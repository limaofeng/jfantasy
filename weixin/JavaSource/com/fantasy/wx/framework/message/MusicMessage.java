package com.fantasy.wx.framework.message;

import com.fantasy.wx.framework.message.content.Music;

/**
 * 音乐消息
 */
public class MusicMessage extends AbstractWeiXinMessage<Music> {

    public MusicMessage(Music content) {
        super(content);
    }

}
