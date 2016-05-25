package org.jfantasy.framework.util.lock.backup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 *分布式锁本地代理类
 */
public class RedisReentrantLock implements Delayed {
    private static final Log logger = LogFactory.getLog(RedisReentrantLock.class);
    private ReentrantLock reentrantLock = new ReentrantLock();

    private RedisLock redisLock;
    private long timeout = 3*60;
    private CountDownLatch lockcount = new CountDownLatch(1);

    private String key;
    private AbstractLockObserver observer;

    private int ref = 0;
    private Object refLock = new Object();
    private boolean destroyed = false;

    private long cleartime = -1;

    public RedisReentrantLock(String key,AbstractLockObserver observer) {
        this.key = key;
        this.observer = observer;
        initWriteLock();
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    private synchronized void initWriteLock(){
        redisLock = new RedisLock(key,new LockListener(){
            @Override
            public void lockAcquired() {
                lockcount.countDown();
            }
            @Override
            public long getExpire() {
                return 0;
            }

            @Override
            public void lockError() {
                                /*synchronized(mutex){
                                        mutex.notify();
                                }*/
                lockcount.countDown();
            }
        },observer);
    }

    public boolean incRef(){
        synchronized(refLock){
            if(destroyed) return false;
            ref ++;
        }
        return true;
    }

    public void descrRef(){
        synchronized(refLock){
            ref --;
        }
    }

    public boolean clear() {
        if(destroyed) return true;
        synchronized(refLock){
            if(ref > 0){
                return false;
            }
            destroyed = true;
            redisLock.clear();
            redisLock = null;
            return true;
        }
    }

    public boolean lock(long timeout) throws LockNotExistsException{
        if(timeout <= 0) timeout = this.timeout;
        //incRef();
        reentrantLock.lock();//多线程竞争时，先拿到第一层锁
        if(redisLock == null){
            reentrantLock.unlock();
            //descrRef();
            throw new LockNotExistsException();
        }
        try{
            lockcount = new CountDownLatch(1);
            boolean res = redisLock.trylock(timeout);
            if(!res){
                lockcount.await(timeout, TimeUnit.SECONDS);
                //mutex.wait(timeout*1000);
                if(!redisLock.doExpire()){
                    reentrantLock.unlock();
                    return false;
                }
            }
            return true;
        }catch(InterruptedException e){
            reentrantLock.unlock();
            return false;
        }
    }

    public boolean lock() throws LockNotExistsException {
        return lock(timeout);
    }

    public boolean unlock(){
        if(!isOwner(true)) {
            try{
                throw new RuntimeException("big ================================================ error.key:" + key);
            }catch(Exception e){
                logger.error("err:" + e,e);
            }
            return false;
        }
        try{
            redisLock.unlock();
            reentrantLock.unlock();//多线程竞争时，释放最外层锁
        }catch(RuntimeException e){
            reentrantLock.unlock();//多线程竞争时，释放最外层锁
            throw e;
        }finally{
            descrRef();
        }
        return canDestroy();
    }

    public boolean canDestroy(){
        synchronized(refLock){
            return ref <= 0;
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isOwner(boolean check) {
        synchronized(refLock){
            if(redisLock == null) {
                logger.error("reidsLock is null:key=" + key);
                return false;
            }
            boolean a = reentrantLock.isHeldByCurrentThread();
            boolean b = redisLock.isOwner();
            if(check){
                if(!a || !b){
                    logger.error(key + ";a:" + a + ";b:"+  b);
                }
            }
            return a && b;
        }
    }

    public boolean setCleartime() {
        synchronized(this){
            if(cleartime>0) return false;
            this.cleartime = System.currentTimeMillis() + 10*1000;
            return true;
        }
    }

    public void resetCleartime(){
        synchronized(this){
            this.cleartime = -1;
        }
    }

    @Override
    public int compareTo(Delayed object) {
        if(object instanceof RedisReentrantLock){
            RedisReentrantLock t = (RedisReentrantLock)object;
            long l = this.cleartime - t.cleartime;

            if(l > 0) return 1 ; //比当前的小则返回1，比当前的大则返回-1，否则为0
            else if(l < 0 ) return -1;
            else return 0;
        }
        return 0;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        long d = unit.convert(cleartime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        return d;
    }

}