package org.jfantasy.framework.util.lock.backup;

public abstract class AbstractLockObserver implements Runnable{

    public abstract boolean tryLock(String key, long expire);

    public void addLockListener(String key, RedisLock redisLock){

    }

    public abstract void removeLockListener(String key);

    public abstract void unLock(String key);
}
