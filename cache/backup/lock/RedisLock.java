package org.jfantasy.framework.util.lock;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


public class RedisLock {

    public static final String LOCK_LUA_SCRIPT;
    public static final String UNLOCK_LUA_SCRIPT;

    static {
        LOCK_LUA_SCRIPT = Config.getAs("redislock.lock_lua_script", String.class);
        UNLOCK_LUA_SCRIPT = Config.getAs("redislock.unlock_lua_script", String.class);
    }

    public static boolean SCRIPT_LOADED = false;
    public static String LOCK_LUA_SCRIPT_SHA;
    public static String UNLOCK_LUA_SCRIPT_SHA;

    public static final long ONE_MILLI_NANOS = 1000000L;
    //默认超时时间（毫秒）
    public static final long DEFAULT_TIME_OUT = 3000;

    public static final Random r = new Random();

    private final String key;

    //持有锁的线程
    private Thread ownerThread;
    //记录重入次数
    private AtomicInteger state;
    //真实的加锁本地时间
    private AtomicLong firstLockTime;
    //最大锁定时间
    private long MAX_LOCK_DURATION=5000;

    private static final ThreadLocal<Map<String, RedisLock>> lockCache = new ThreadLocal<Map<String, RedisLock>>();

    private RedisLock(String key) {
        this.key = key;
        state=new AtomicInteger(0);
        firstLockTime=new AtomicLong(0);
    }

    public static RedisLock getLock(String key) {
        if (!SCRIPT_LOADED) {
            loadScript();
            SCRIPT_LOADED = true;
        }
        Map<String, RedisLock> map = lockCache.get();
        if (map == null) {
            map = new HashMap<String, RedisLock>();
            lockCache.set(map);
        }
        RedisLock lock = map.get(key);
        if (lock == null) {
            lock = new RedisLock(key);
            map.put(key, lock);
        }
        return lock;
    }

    private static void loadScript() {
        //这个就是负责取出一个Jedis连接
        WNoSql.getInstance().run(new NoSql() {
            @Override
            public void run(Jedis j) {
                LOCK_LUA_SCRIPT_SHA = j.scriptLoad(LOCK_LUA_SCRIPT);
                UNLOCK_LUA_SCRIPT_SHA = j.scriptLoad(UNLOCK_LUA_SCRIPT);
                SCRIPT_LOADED = true;
            }
        });
    }

    public boolean isLocked() {
        return state.intValue() > 0;
    }

    public boolean isHeldByCurrentThread() {
        return Thread.currentThread() == ownerThread;
    }

    public boolean lock(final long timeout) {
        if (isHeldByCurrentThread()) {
            //如果是当前线程持有锁 则加锁数+1
            state.incrementAndGet();
            return true;
        }
        return WNoSql.getInstance().run(new NoSqlCallBack<Boolean>() {
            @Override
            public Boolean run(Jedis j) {
                long timestamp = System.currentTimeMillis();
                long nanoTime = System.nanoTime();
                long localTimeOut = timeout * ONE_MILLI_NANOS;
                while ((System.nanoTime() - nanoTime) < localTimeOut) {
                    Object obj = j.evalsha(LOCK_LUA_SCRIPT_SHA, 1,key, String.valueOf(timestamp), String.valueOf(MAX_LOCK_DURATION));
                    if (obj instanceof Long) {
                        long ret = (Long) obj;
                        if (ret == 0) { //锁成功了
                            ownerThread=Thread.currentThread();
                            firstLockTime.set(timestamp);
                            state.incrementAndGet();
                            return true;
                        } else if (ret == 1) {//失败了 可以进行退让等待
                            try {
                                Thread.sleep(2, Rnd.nextInt(500));
                            } catch (InterruptedException e) {
                                throw new IllegalStateException(new StringBuilder("Redis Lock operate failed. ").toString(),e);
                            }
                        }
                    }else{
                        throw new IllegalStateException(new StringBuilder("Type of Redis<span></span> Lock Key: ").append(key).append(" isn't string").toString());
                    }

                }
                return false;
            }
        });
    }

    public boolean lock() {
        return lock(DEFAULT_TIME_OUT);
    }

    /**
     * 解锁
     * @return 如果锁定时间超过最大所持续时间
     */
    public boolean unlock() {
        if(isHeldByCurrentThread()){
            final byte[] isTimeOut=new byte[1];
            Bits.putBoolean(isTimeOut, 0, false);
            int lockCount=state.decrementAndGet();
            final long timestamp = System.currentTimeMillis();
            if(timestamp-firstLockTime.get()>MAX_LOCK_DURATION){
                Bits.putBoolean(isTimeOut,0,true);
            }
            if(lockCount==0){
                WNoSql.getInstance().run(new NoSql() {
                    @Override
                    public void run(Jedis j) {
                        Object obj = j.evalsha(UNLOCK_LUA_SCRIPT_SHA, 1,key, String.valueOf(timestamp), String.valueOf(MAX_LOCK_DURATION));
                        System.out.println(obj);
                        if (obj instanceof Long) {
                            long ret = (Long) obj;
                            if (ret == 0) {
                                ownerThread=null;
                                firstLockTime.set(0);
                            } else if (ret == 1) {
                                Bits.putBoolean(isTimeOut,0,true);
                            }
                        }
                    }
                });
            }
            return Bits.getBoolean(isTimeOut, 0);
        }
        throw new IllegalMonitorStateException("Lock must be locked on current thread.");
    }
}
