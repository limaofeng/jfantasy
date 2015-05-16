package com.fantasy.framework.dao.hibernate.cache.redis.regions;

import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.TransactionalDataRegion;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

/**
 * An Ehcache specific TransactionalDataRegion.
 * <p/>
 * This is the common superclass entity and collection regions.
 *
 * @author Chris Dennis
 * @author Greg Luck
 * @author Emmanuel Bernard
 * @author Abhishek Sanoujam
 * @author Alex Snaps
 */
public class RedisCacheTransactionalDataRegion extends RedisCacheDataRegion implements TransactionalDataRegion {
    private static final int LOCAL_LOCK_PROVIDER_CONCURRENCY = 128;

    private final Settings settings;

    protected final CacheDataDescription metadata;

    RedisCacheTransactionalDataRegion(RedisCacheAccessStrategyFactory accessStrategyFactory, Cache cache, Settings settings,
                                      CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory,cache, properties);
        this.settings = settings;
        this.metadata = metadata;
        final Object context = cache.getNativeCache();
        System.out.println("--------------");
        System.out.println(context);
    }

    public Settings getSettings() {
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
        return getCache().get(key);
        /*
        try {
            final Element element = getCache().get( key );
            if ( element == null ) {
                return null;
            }
            else {
                return element.getObjectValue();
            }
        }*/
    }

    /**
     * Map the given value to the given key, replacing any existing mapping for this key
     *
     * @param key   The cache key
     * @param value The data to cache
     * @throws CacheException Indicates a problem accessing the cache
     */
    public final void put(Object key, Object value) throws CacheException {
        try {
            getCache().put(key, value);
        } catch (IllegalArgumentException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }

    /**
     * Remove the mapping for this key (if any exists).
     *
     * @param key The cache key
     * @throws CacheException Indicates a problem accessing the cache
     */
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
