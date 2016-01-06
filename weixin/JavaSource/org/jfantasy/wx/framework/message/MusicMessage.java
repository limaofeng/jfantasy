package org.jfantasy.wx.framework.message;

import org.jfantasy.wx.framework.message.content.Music;

/**
 * 音乐消息
 */
public class MusicMessage extends AbstractWeiXinMessage<Music> {

    public MusicMessage(Music content) {
        super(content);
    }

}
