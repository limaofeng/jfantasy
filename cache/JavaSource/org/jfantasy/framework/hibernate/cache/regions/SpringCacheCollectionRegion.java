package org.jfantasy.framework.hibernate.cache.regions;


import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheCollectionRegion extends SpringCacheTransactionalDataRegion implements CollectionRegion {

    public SpringCacheCollectionRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Settings settings, CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory, underlyingCache, settings, metadata, properties);
    }

    @Override
    public CollectionRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createCollectionRegionAccessStrategy(this, accessType);
    }

}
