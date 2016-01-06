package org.jfantasy.framework.hibernate.cache.regions;


import org.jfantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheQueryResultsRegion extends SpringCacheGeneralDataRegion implements QueryResultsRegion {

    public SpringCacheQueryResultsRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Properties properties) {
        super( accessStrategyFactory, underlyingCache, properties );
    }

}