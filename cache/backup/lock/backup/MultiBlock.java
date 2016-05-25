package org.jfantasy.framework.util.lock.backup;

import redis.clients.jedis.Transaction;


public abstract class MultiBlock {

    protected Transaction transaction;

    public abstract void execute();

}
