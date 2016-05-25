package org.jfantasy.framework.util.lock.backup;

/**
 *使用Redis实现的分布式锁
 *基本工作原理如下：
 *1. 使用setnx(key,时间戮 超时)，如果设置成功，则直接拿到锁
 *2. 如果设置不成功，获取key的值v1（它的到期时间戮）,跟当前时间对比，看是否已经超时
 *3. 如果超时（说明拿到锁的结点已经挂掉)，v2=getset(key,时间戮 超时 1)，判断v2是否等于v1，如果相等，加锁成功，否则加锁失败，等过段时间再重试(200MS)
 */
public class RedisLock implements LockListener{
    private String key;
    private boolean owner = false;
    private AbstractLockObserver observer = null;
    private LockListener lockListener = null;
    private boolean waiting = false;
    private long expire;//锁超时时间，以秒为单位
    private boolean expired = false;

    public RedisLock(String key, LockListener lockListener, AbstractLockObserver observer) {
        this.key = key;
        this.lockListener = lockListener;
        this.observer = observer;
    }

    public boolean trylock(long expire) {
        synchronized(this){
            if(owner){
                return true;
            }
            this.expire = expire;
            this.expired = false;
            if(!waiting){
                owner = observer.tryLock(key,expire);
                if(!owner){
                    waiting = true;
                    observer.addLockListener(key, this);
                }
            }
            return owner;
        }
    }

    public boolean isOwner() {
        return owner;
    }

    public void unlock() {
        synchronized(this){
            observer.unLock(key);
            owner = false;
        }
    }

    public void clear() {
        synchronized(this){
            if(waiting) {
                observer.removeLockListener(key);
                waiting = false;
            }
        }
    }

    public boolean doExpire(){
        synchronized(this){
            if(owner) return true;
            if(expired) return false;
            expired = true;
            clear();
        }
        return false;
    }

    @Override
    public void lockAcquired() {
        synchronized(this){
            if(expired){
                unlock();
                return;
            }
            owner = true;
            waiting = false;
        }
        lockListener.lockAcquired();
    }

    @Override
    public long getExpire() {
        return this.expire;
    }

    @Override
    public void lockError() {
        synchronized(this){
            owner = false;
            waiting = false;
            lockListener.lockError();
        }
    }

}