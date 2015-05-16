package com.fantasy.framework.dao.hibernate.cache.redis.strategy;

import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheCollectionRegion;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheEntityRegion;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

public interface RedisCacheAccessStrategyFactory {

    EntityRegionAccessStrategy createEntityRegionAccessStrategy(RedisCacheEntityRegion entityRegion,AccessType accessType);

    CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(RedisCacheCollectionRegion collectionRegion, AccessType accessType);

    NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(RedisCacheNaturalIdRegion naturalIdRegion,AccessType accessType);

}
