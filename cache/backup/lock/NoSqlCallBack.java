package org.jfantasy.framework.util.lock;


import redis.clients.jedis.Jedis;

public class NoSqlCallBack<B> extends NoSql{

    @Override
    public Object run(Jedis j) {
        return null;
    }

}
