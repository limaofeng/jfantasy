package com.fantasy.framework.dao.hibernate.cache.redis.regions;


import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

public class RedisCacheCollectionRegion extends RedisCacheTransactionalDataRegion implements CollectionRegion {

    public RedisCacheCollectionRegion(
            RedisCacheAccessStrategyFactory accessStrategyFactory,
            Cache underlyingCache,
            Settings settings,
            CacheDataDescription metadata,
            Properties properties) {
        super( accessStrategyFactory, underlyingCache, settings, metadata, properties );
    }

    @Override
    public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createCollectionRegionAccessStrategy( this, accessType );
    }

}
