package com.fantasy.wx.listener;

import com.fantasy.wx.framework.event.SubscribeEventListener;
import com.fantasy.wx.framework.message.EventMessage;
import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;
import com.fantasy.wx.service.UserInfoWeiXinService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by zzzhong on 2015/1/6.
 */
public class SubscribeListener implements SubscribeEventListener {
    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    @Override
    public void onSubscribe(WeiXinSession session, Event event,EventMessage message) {
        //UserInfo ui=userInfoWeiXinService.getUserInfo(message.getFromUserName());
    }
}
