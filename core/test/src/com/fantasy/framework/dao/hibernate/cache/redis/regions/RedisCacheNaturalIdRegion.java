package com.fantasy.framework.dao.hibernate.cache.redis.regions;


import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

public class RedisCacheNaturalIdRegion extends RedisCacheTransactionalDataRegion implements NaturalIdRegion {

    public RedisCacheNaturalIdRegion(RedisCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Settings settings, CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory, underlyingCache, settings, metadata, properties);
    }

    @Override
    public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createNaturalIdRegionAccessStrategy(this, accessType);
    }
}