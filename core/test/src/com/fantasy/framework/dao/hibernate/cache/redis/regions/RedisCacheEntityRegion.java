package com.fantasy.framework.dao.hibernate.cache.redis.regions;

import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

public class RedisCacheEntityRegion extends RedisCacheTransactionalDataRegion implements EntityRegion {

    public RedisCacheEntityRegion(RedisCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Settings settings, CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory,underlyingCache, settings, metadata, properties);
    }

    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createEntityRegionAccessStrategy( this, accessType );
    }

}
