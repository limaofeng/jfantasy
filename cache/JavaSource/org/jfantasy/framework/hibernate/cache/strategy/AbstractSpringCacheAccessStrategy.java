package org.jfantasy.framework.hibernate.cache.strategy;

import org.jfantasy.framework.hibernate.cache.regions.SpringCacheTransactionalDataRegion;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;


abstract class AbstractSpringCacheAccessStrategy<T extends SpringCacheTransactionalDataRegion> {

    private final T region;
    private final Settings settings;

    AbstractSpringCacheAccessStrategy(T region, Settings settings) {
        this.region = region;
        this.settings = settings;
    }

    protected T region() {
        return region;
    }

    protected Settings settings() {
        return settings;
    }

    public final boolean putFromLoad(Object key, Object value, long txTimestamp, Object version) throws CacheException {
        return putFromLoad( key, value, txTimestamp, version, settings.isMinimalPutsEnabled() );
    }

    public abstract boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride)
            throws CacheException;

    public final SoftLock lockRegion() {
        return null;
    }

    public final void unlockRegion(SoftLock lock) throws CacheException {
        region.clear();
    }

    public void remove(Object key) throws CacheException {
    }

    public final void removeAll() throws CacheException {
        region.clear();
    }

    public final void evict(Object key) throws CacheException {
        region.remove( key );
    }

    public final void evictAll() throws CacheException {
        region.clear();
    }

}
