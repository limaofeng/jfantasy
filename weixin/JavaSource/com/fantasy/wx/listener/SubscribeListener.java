package com.fantasy.wx.listener;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.wx.framework.event.SubscribeEventListener;
import com.fantasy.wx.framework.message.EventMessage;
import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;
import com.fantasy.wx.service.UserInfoWeiXinService;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executor;

public class SubscribeListener implements SubscribeEventListener {

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    @Override
    public void onSubscribe(final WeiXinSession session, Event event, final EventMessage message) {
        Executor executor = SpringContextUtil.getBeanByType(Executor.class);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                userInfoWeiXinService.refresh(message.getFromUserName());
            }
        });
    }
}
