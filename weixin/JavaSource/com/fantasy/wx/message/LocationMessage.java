package com.fantasy.wx.message;

import com.fantasy.wx.message.content.Location;

import java.util.Date;

/**
 * 地理位置消息
 */
public class LocationMessage extends AbstractWeiXinMessage<Location> {

    public LocationMessage(Long id, String fromUserName, Date createTime) {
        super(id, fromUserName, createTime);
    }

}
