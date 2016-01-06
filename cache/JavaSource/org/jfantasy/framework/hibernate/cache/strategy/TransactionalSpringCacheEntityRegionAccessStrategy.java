package org.jfantasy.framework.hibernate.cache.strategy;

import org.jfantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;


public class TransactionalSpringCacheEntityRegionAccessStrategy extends AbstractSpringCacheAccessStrategy<SpringCacheEntityRegion> implements EntityRegionAccessStrategy {

    private final Cache cache;

    public TransactionalSpringCacheEntityRegionAccessStrategy(
            SpringCacheEntityRegion region,
            Cache cache,
            Settings settings) {
        super(region, settings);
        this.cache = cache;
    }

    @Override
    public boolean afterInsert(Object key, Object value, Object version) {
        return false;
    }

    @Override
    public boolean afterUpdate(Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) {
        return false;
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
        final Object element = cache.get(key);
        return element == null ? null : element;
    }

    @Override
    public EntityRegion getRegion() {
        return region();
    }

    @Override
    public boolean insert(Object key, Object value, Object version) throws CacheException {
        cache.put(key, value);
        return true;
    }

    @Override
    public SoftLock lockItem(Object key, Object version) throws CacheException {
        return null;
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        if (minimalPutOverride && cache.get(key) != null) {
            return false;
        }
        cache.put(key, value);
        return true;
    }

    @Override
    public void remove(Object key) throws CacheException {
        cache.evict(key);
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) throws CacheException {
    }

    @Override
    public boolean update(Object key, Object value, Object currentVersion, Object previousVersion) throws CacheException {
        cache.put(key, value);
        return true;
    }

}
