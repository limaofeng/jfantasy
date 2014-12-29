package com.fantasy.wx.factory;

import com.fantasy.wx.exception.NoSessionException;
import com.fantasy.wx.exception.WeiXinException;
import com.fantasy.wx.session.WeiXinSession;

public class WeiXinSessionUtils {

    /**
     * 当前 session 对象
     */
    private static ThreadLocal<WeiXinSession> current = new ThreadLocal<WeiXinSession>();

    public static WeiXinSession getCurrentSession() throws WeiXinException {
        if (current.get() == null) {
            throw new NoSessionException("未初始化 WeiXinSession 对象");
        }
        return current.get();
    }

    protected static WeiXinSession saveSession(WeiXinSession session) {
        current.set(session);
        return session;
    }
}
