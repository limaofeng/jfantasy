package org.jfantasy.wx.framework.factory;

import org.jfantasy.wx.framework.exception.NoSessionException;
import org.jfantasy.wx.framework.exception.WeiXinException;
import org.jfantasy.wx.framework.session.WeiXinSession;

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

    public static WeiXinSession saveSession(WeiXinSession session) {
        current.set(session);
        return session;
    }

    public static void closeSession() {
        current.remove();
    }
}
