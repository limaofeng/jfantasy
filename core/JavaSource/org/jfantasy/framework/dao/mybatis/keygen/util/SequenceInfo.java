package org.jfantasy.framework.dao.mybatis.keygen.util;

import org.jfantasy.framework.dao.mybatis.keygen.service.SequenceService;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 序列信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-8-24 上午10:40:58
 */
public class SequenceInfo {

    private final static Log LOG = LogFactory.getLog(SequenceInfo.class);

    private final static ConcurrentMap<String, SequenceInfo> keys = new ConcurrentHashMap<String, SequenceInfo>(10);
    private final static Lock retrievelock = new ReentrantLock();

    private Lock lock = new ReentrantLock();
    private SequenceService service;
    private long keyMax = 1L;
    private long keyMin;
    private long nextKey = 0L;
    private long poolSize;
    private String keyName;

    protected static SequenceInfo retrieve(SequenceService service, long poolSize, String keyName) {
        try {
            retrievelock.lock();
            if (!keys.containsKey(keyName)) {
                keys.put(keyName, new SequenceInfo(service, poolSize, keyName));
            }
            return keys.get(keyName);
        } finally {
            retrievelock.unlock();
        }
    }

    /**
     * 提供直接通过 SequenceInfo 直接查询索引的方法
     *
     * @param keyName KeyName
     * @return long
     */
    public static long nextValue(String keyName) {
        DataBaseKeyGenerator dataBaseKeyGenerator = SpringContextUtil.getBeanByType(DataBaseKeyGenerator.class);
        assert dataBaseKeyGenerator != null;
        return dataBaseKeyGenerator.nextValue(keyName);
    }

    private SequenceInfo(SequenceService service, long poolSize, String keyName) {
        this.service = service;
        this.poolSize = poolSize;
        this.keyName = keyName;
        try {
            this.lock.lock();
            retrieveFromDB();
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 从数据库检索
     */
    public void retrieveFromDB() {
        long keyFromDB;
        if ((this.service.exists(this.keyName)) || (this.nextKey > this.keyMax)) {
            keyFromDB = getKeyinfo(this.keyName, this.poolSize);
        } else {
            keyFromDB = createKey(this.keyName, this.poolSize);
        }
        this.keyMax = keyFromDB;
        this.keyMin = (this.keyMax - this.poolSize + 1L);
        this.nextKey = this.keyMin;
    }

    /**
     * 获取序列的下个值
     *
     * @param keyName  keyName
     * @param poolSize 缓冲值
     * @return long
     */
    private long getKeyinfo(String keyName, long poolSize) {
        return this.service.next(keyName, poolSize);
    }

    /**
     * 创建新的序列
     *
     * @param keyName  keyName
     * @param poolSize 缓冲值
     * @return long
     */
    private long createKey(String keyName, long poolSize) {
        return this.service.newkey(keyName, poolSize);
    }

    /**
     * 获取序列的下一个值
     *
     * @return long
     */
    public long nextValue() {
        try {
            this.lock.lock();
            if (this.nextKey > this.keyMax) {
                retrieveFromDB();
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug(this.keyName + " nextKey = " + (this.nextKey) + "\tpoolSize = " + this.poolSize + "\tkeyMax = " + keyMax);
            }
            return this.nextKey++;
        } finally {
            this.lock.unlock();
        }
    }

    /**
     * 获取缓存序列的最大值
     *
     * @return long
     */
    public long getKeyMax() {
        return this.keyMax;
    }

    /**
     * 获取缓存序列的最小值
     *
     * @return long
     */
    public long getKeyMin() {
        return this.keyMin;
    }

    public long getPoolSize() {
        return this.poolSize;
    }

    public void setPoolSize(long poolSize) {
        this.poolSize = poolSize;
    }

    public String getKeyName() {
        return this.keyName;
    }

}