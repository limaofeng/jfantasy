package org.jfantasy.framework.hibernate.cache.regions;

import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.CacheDataDescription;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheEntityRegion extends SpringCacheTransactionalDataRegion implements EntityRegion {

    public SpringCacheEntityRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, SessionFactoryOptions settings, CacheDataDescription metadata, Properties properties) {
        super(accessStrategyFactory,underlyingCache, settings, metadata, properties);
    }

    @Override
    public EntityRegionAccessStrategy buildAccessStrategy(AccessType accessType) throws CacheException {
        return getAccessStrategyFactory().createEntityRegionAccessStrategy( this, accessType );
    }

}
