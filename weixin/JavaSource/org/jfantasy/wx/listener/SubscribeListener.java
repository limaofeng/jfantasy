package org.jfantasy.wx.listener;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.wx.framework.event.SubscribeEventListener;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.factory.WeiXinSessionUtils;
import org.jfantasy.wx.framework.message.EventMessage;
import org.jfantasy.wx.framework.message.content.Event;
import org.jfantasy.wx.framework.session.WeiXinSession;
import org.jfantasy.wx.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Executor;

public class SubscribeListener implements SubscribeEventListener {

    private final static Log LOG = LogFactory.getLog(SubscribeListener.class);

    @Autowired
    private UserService userService;

    @Override
    public void onSubscribe(final WeiXinSession session, Event event, final EventMessage message) {
        Executor executor = SpringContextUtil.getBeanByType(Executor.class);
        assert executor != null;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    WeiXinSessionUtils.saveSession(session);
                    userService.checkCreateMember(session.getAccountDetails().getAppId(),message.getFromUserName());
                } catch (WeiXinException e) {
                    LOG.error(e.getMessage(), e);
                } finally {
                    WeiXinSessionUtils.closeSession();
                }
            }
        });
    }
}
