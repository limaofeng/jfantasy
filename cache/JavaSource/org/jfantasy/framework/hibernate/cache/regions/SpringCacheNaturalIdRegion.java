package org.jfantasy.framework.hibernate.cache.regions;


import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.NaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheNaturalIdRegion extends SpringCacheTransactionalDataRegion implements NaturalIdRegion {

    public SpringCacheNaturalIdRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Settings settings, CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory, underlyingCache, settings, metadata, properties);
    }

    @Override
    public NaturalIdRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createNaturalIdRegionAccessStrategy(this, accessType);
    }
}