package com.fantasy.framework.dao.hibernate.cache.redis.strategy;


import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheCollectionRegion;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;

public class ReadOnlyRedisCacheCollectionRegionAccessStrategy extends AbstractRedisCacheAccessStrategy<RedisCacheCollectionRegion> implements CollectionRegionAccessStrategy {

    public ReadOnlyRedisCacheCollectionRegionAccessStrategy(RedisCacheCollectionRegion region, Settings settings) {
        super(region, settings);
    }

    @Override
    public CollectionRegion getRegion() {
        return region();
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
        return region().get(key);
    }

    @Override
    public boolean putFromLoad(Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        if (minimalPutOverride && region().contains(key)) {
            return false;
        } else {
            region().put(key, value);
            return true;
        }
    }

    @Override
    public SoftLock lockItem(Object key, Object version) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public void unlockItem(Object key, SoftLock lock) throws CacheException {
    }

}