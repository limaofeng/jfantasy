package com.fantasy.framework.dao.hibernate.cache.redis.strategy;


import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheCollectionRegion;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

public class TransactionalRedisCacheCollectionRegionAccessStrategy extends AbstractRedisCacheAccessStrategy<RedisCacheCollectionRegion> implements CollectionRegionAccessStrategy {

    private final Cache cache;

    public TransactionalRedisCacheCollectionRegionAccessStrategy(RedisCacheCollectionRegion region, Cache cache, Settings settings) {
        super(region, settings);
        this.cache = cache;
    }

    @Override
    public Object get(Object key, long txTimestamp) throws CacheException {
        return cache.get(key);
    }

    @Override
    public CollectionRegion getRegion() {
        return region();
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

}