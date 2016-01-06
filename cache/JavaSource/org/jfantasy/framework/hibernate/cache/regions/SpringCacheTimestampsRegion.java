package org.jfantasy.framework.hibernate.cache.regions;

import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheTimestampsRegion extends SpringCacheGeneralDataRegion implements TimestampsRegion {

    public SpringCacheTimestampsRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, properties);
    }

}
