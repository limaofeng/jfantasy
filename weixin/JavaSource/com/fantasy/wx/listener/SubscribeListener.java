package com.fantasy.wx.listener;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.wx.framework.event.SubscribeEventListener;
import com.fantasy.wx.framework.exception.WeiXinException;
import com.fantasy.wx.framework.factory.WeiXinSessionUtils;
import com.fantasy.wx.framework.message.EventMessage;
import com.fantasy.wx.framework.message.content.Event;
import com.fantasy.wx.framework.session.WeiXinSession;
import com.fantasy.wx.service.UserInfoWeiXinService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executor;

public class SubscribeListener implements SubscribeEventListener {

    private final static Log LOG = LogFactory.getLog(SubscribeListener.class);

    @Autowired
    private UserInfoWeiXinService userInfoWeiXinService;

    @Override
    public void onSubscribe(final WeiXinSession session, Event event, final EventMessage message) {
        Executor executor = SpringContextUtil.getBeanByType(Executor.class);
        assert executor != null;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    WeiXinSessionUtils.saveSession(session);
                    userInfoWeiXinService.checkCreateMember(message.getFromUserName());
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                } finally {
                    WeiXinSessionUtils.closeSession();
                }
            }
        });
    }
}
