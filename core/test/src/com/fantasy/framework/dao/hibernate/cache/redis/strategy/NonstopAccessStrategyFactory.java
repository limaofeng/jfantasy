package com.fantasy.framework.dao.hibernate.cache.redis.strategy;

import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheCollectionRegion;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheEntityRegion;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheNaturalIdRegion;
import org.hibernate.cache.ehcache.internal.nonstop.HibernateNonstopCacheExceptionHandler;
import org.hibernate.cache.ehcache.internal.nonstop.NonstopAwareCollectionRegionAccessStrategy;
import org.hibernate.cache.ehcache.internal.nonstop.NonstopAwareEntityRegionAccessStrategy;
import org.hibernate.cache.ehcache.internal.nonstop.NonstopAwareNaturalIdRegionAccessStrategy;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;


public class NonstopAccessStrategyFactory implements RedisCacheAccessStrategyFactory {

    private final RedisCacheAccessStrategyFactory actualFactory;

    public NonstopAccessStrategyFactory(RedisCacheAccessStrategyFactory actualFactory) {
        this.actualFactory = actualFactory;
    }

    @Override
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(
            RedisCacheEntityRegion entityRegion,
            AccessType accessType) {
        return new NonstopAwareEntityRegionAccessStrategy(
                actualFactory.createEntityRegionAccessStrategy(entityRegion, accessType),
                HibernateNonstopCacheExceptionHandler.getInstance()
        );
    }

    @Override
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(
            RedisCacheNaturalIdRegion naturalIdRegion,
            AccessType accessType) {
        return new NonstopAwareNaturalIdRegionAccessStrategy(
                actualFactory.createNaturalIdRegionAccessStrategy(
                        naturalIdRegion,
                        accessType
                ), HibernateNonstopCacheExceptionHandler.getInstance()
        );
    }

    @Override
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(
            RedisCacheCollectionRegion collectionRegion,
            AccessType accessType) {
        return new NonstopAwareCollectionRegionAccessStrategy(
                actualFactory.createCollectionRegionAccessStrategy(
                        collectionRegion,
                        accessType
                ), HibernateNonstopCacheExceptionHandler.getInstance()
        );
    }

}
