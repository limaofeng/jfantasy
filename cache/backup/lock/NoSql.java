package org.jfantasy.framework.util.lock;

import redis.clients.jedis.Jedis;

/**
 * Created by limaofeng on 16/5/23.
 */
public abstract class NoSql<T> implements Runnable{

    protected String LOCK_LUA_SCRIPT_SHA;
    protected String UNLOCK_LUA_SCRIPT_SHA;
    protected boolean SCRIPT_LOADED;

    @Override
    public void run() {
        this.run(null);
    }

    public abstract T run(Jedis j);

}
