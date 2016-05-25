package org.jfantasy.framework.util.lock.backup;

import redis.clients.util.SafeEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

public class LockObserver extends AbstractLockObserver implements Runnable{
    private CacheRedisClient client;
    private Object mutex = new Object();
    private Map<String,LockListener> lockMap = new ConcurrentHashMap();
    private boolean stoped = false;
    private long interval = 500;
    private boolean terminated = false;
    private CountDownLatch doneSignal = new CountDownLatch(1);

    public LockObserver(String schema){
        client = new CacheRedisClient(schema);

        SystemExitListener.addTerminateListener(new ExitHandler(){
            public void run() {
                stoped = true;
                try {
                    doneSignal.await();
                } catch (InterruptedException e) {
                }
            }
        });
    }


    public void addLockListener(String key,LockListener listener){
        if(terminated){
            listener.lockError();
            return;
        }
        synchronized(mutex){
            lockMap.put(key, listener);
        }
    }

    public void removeLockListener(String key){
        synchronized(mutex){
            lockMap.remove(key);
        }
    }

    @Override
    public void run() {
        while(!terminated){
            long p1 = System.currentTimeMillis();
            Map<String,LockListener> clone = new HashMap<>();
            synchronized(mutex){
                clone.putAll(lockMap);
            }
            Set<String> keyset = clone.keySet();
            if(keyset.size() > 0){
                ConnectionFactory.setSingleConnectionPerThread(keyset.size());
                for(String key : keyset){
                    LockListener ll = clone.get(key);
                    try{
                        if(tryLock(key,ll.getExpire())) {
                            ll.lockAcquired();
                            removeLockListener(key);
                        }
                    }catch(Exception e){
                        ll.lockError();
                        removeLockListener(key);
                    }
                }
                ConnectionFactory.releaseThreadConnection();
            }else{
                if(stoped){
                    terminated = true;
                    doneSignal.countDown();
                    return;
                }
            }
            try {
                long p2 = System.currentTimeMillis();
                long cost = p2 - p1;
                if(cost <= interval){
                    Thread.sleep(interval - cost);
                }else{
                    Thread.sleep(interval*2);
                }
            } catch (InterruptedException e) {
            }
        }

    }


    /**
     * 超时时间单位为s!!!
     * @param key
     * @param expireInSecond
     * @return
     */
    public boolean tryLock(final String key,final long expireInSecond){
        if(terminated) return false;
        final long tt = System.currentTimeMillis();
        final long expire = expireInSecond * 1000;
        final Long ne = tt - expire;
        List<Object> mm = client.multi(key, new MultiBlock(){
            @Override
            public void execute() {
                transaction.setnx(key, String.valueOf(ne));
                transaction.get(SafeEncoder.encode(key));
            }
        });
        Long res = (Long)mm.get(0);
        if(new Long(1).equals(res)) {
            return true;
        }else{
            byte[] bb = (byte[])mm.get(1);
            Long ex = client.deserialize(bb);
            if(ex == null || tt > ex){
                Long old = client.getSet(key, new Long(ne - 1));
                if(old == null || (ex == null&&old==null) || (ex!=null&&ex.equals(old))){
                    return true;
                }
            }
        }
        return false;
    }

    public void unLock(String key){
        client.del(key);
    }
}