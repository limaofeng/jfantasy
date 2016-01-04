package org.jfantasy.framework.hibernate.cache.nonstop;


import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;

public class NonstopAwareCollectionRegionAccessStrategy implements CollectionRegionAccessStrategy {
    private final CollectionRegionAccessStrategy actualStrategy;
    private final HibernateNonstopCacheExceptionHandler hibernateNonstopExceptionHandler;

    public NonstopAwareCollectionRegionAccessStrategy(CollectionRegionAccessStrategy actualStrategy, HibernateNonstopCacheExceptionHandler hibernateNonstopExceptionHandler) {
        this.actualStrategy = actualStrategy;
        this.hibernateNonstopExceptionHandler = hibernateNonstopExceptionHandler;
    }

    @Override
    public CollectionRegion getRegion() {
        return actualStrategy.getRegion();
    }

    @Override
    public void evict(Object key) throws CacheException {
        try {
            actualStrategy.evict(key);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
        }
    }

    @Override
    public void evictAll() throws CacheException {
        try {
            actualStrategy.evictAll();
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
        }
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
        try {
            return actualStrategy.get(key, txTimestamp);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
            return null;
        }
    }

    @Override
    public SoftLock lockItem(Object key, Object version) throws CacheException {
        try {
            return actualStrategy.lockItem(key, version);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
            return null;
        }
    }

    @Override
    public SoftLock lockRegion() throws CacheException {
        try {
            return actualStrategy.lockRegion();
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
            return null;
        }
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride)
            throws CacheException {
        try {
            return actualStrategy.putFromLoad(key, value, txTimestamp, version, minimalPutOverride);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
            return false;
        }
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
        try {
            return actualStrategy.putFromLoad(key, value, txTimestamp, version);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
            return false;
        }
    }

    @Override
    public void remove(Object key) throws CacheException {
        try {
            actualStrategy.remove(key);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
        }
    }

    @Override
    public void removeAll() throws CacheException {
        try {
            actualStrategy.removeAll();
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
        }
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) throws CacheException {
        try {
            actualStrategy.unlockItem(key, lock);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
        }
    }

    @Override
    public void unlockRegion(SoftLock lock) throws CacheException {
        try {
            actualStrategy.unlockRegion(lock);
        } catch (NonStopCacheException nonStopCacheException) {
            hibernateNonstopExceptionHandler.handleNonstopCacheException(nonStopCacheException);
        }
    }

}