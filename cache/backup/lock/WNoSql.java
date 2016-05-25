package org.jfantasy.framework.util.lock;

/**
 * Created by limaofeng on 16/5/23.
 */
public class WNoSql {
    private static WNoSql instance;

    public static WNoSql getInstance() {
        return instance;
    }

    public <T> T run(NoSql noSql) {
        return null;
    }
}
