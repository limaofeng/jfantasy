package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Location;

/**
 * 地理位置消息
 */
public class LocationMessage extends AbstractWeiXinMessage<Location> {
    @Override
    public Location getContent() {
        return null;
    }
}
