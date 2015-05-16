package com.fantasy.framework.dao.hibernate.cache.redis.regions;


import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.spi.QueryResultsRegion;
import org.springframework.cache.Cache;

import java.util.Properties;

public class RedisCacheQueryResultsRegion extends RedisCacheGeneralDataRegion implements QueryResultsRegion {

    public RedisCacheQueryResultsRegion(RedisCacheAccessStrategyFactory accessStrategyFactory,Cache underlyingCache,Properties properties) {
        super( accessStrategyFactory, underlyingCache, properties );
    }

}