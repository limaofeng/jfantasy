package com.fantasy.framework.dao.hibernate.cache.redis.regions;

import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.spi.TimestampsRegion;
import org.springframework.cache.Cache;

import java.util.Properties;

public class RedisCacheTimestampsRegion extends RedisCacheGeneralDataRegion implements TimestampsRegion {

    public RedisCacheTimestampsRegion(RedisCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, properties);
    }

}
