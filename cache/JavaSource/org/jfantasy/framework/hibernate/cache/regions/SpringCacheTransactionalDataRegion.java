package org.jfantasy.framework.hibernate.cache.regions;

import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheTransactionalDataRegion extends SpringCacheDataRegion implements TransactionalDataRegion {

    private final SessionFactoryOptions settings;

    protected final CacheDataDescription metadata;

    SpringCacheTransactionalDataRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache cache, SessionFactoryOptions settings, CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory, cache, properties);
        this.settings = settings;
        this.metadata = metadata;
    }

    public SessionFactoryOptions getSettings() {
        return settings;
    }

    @Override
    public boolean isTransactionAware() {
        return false;
    }

    @Override
    public CacheDataDescription getCacheDataDescription() {
        return metadata;
    }

    public final Object get(Object key) {
        final Cache.ValueWrapper element = getCache().get(key);
        if (element == null) {
            return null;
        } else {
            return element.get();
        }
    }

    public final void put(Object key, Object value) throws CacheException {
        try {
            getCache().put(key, value);
        } catch (IllegalArgumentException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }

    public final void remove(Object key) throws CacheException {
        try {
            getCache().evict(key);
        } catch (ClassCastException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }

    public final void clear() throws CacheException {
        try {
            getCache().clear();
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }

    public final void writeLock(Object key) throws CacheException {
    }

    public final void writeUnlock(Object key) throws CacheException {
    }

    public final void readLock(Object key) throws CacheException {
    }

    public final void readUnlock(Object key) throws CacheException {

    }

    public final boolean locksAreIndependentOfCache() {
        return false;
    }

}
