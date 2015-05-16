package com.fantasy.framework.dao.hibernate.cache.redis.strategy;


import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheCollectionRegion;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;

public class ReadWriteRedisCacheCollectionRegionAccessStrategy extends AbstractReadWriteRedisCacheAccessStrategy<RedisCacheCollectionRegion> implements CollectionRegionAccessStrategy {

    public ReadWriteRedisCacheCollectionRegionAccessStrategy(RedisCacheCollectionRegion region, Settings settings) {
        super(region, settings);
    }

    @Override
    public CollectionRegion getRegion() {
        return region();
    }

}
