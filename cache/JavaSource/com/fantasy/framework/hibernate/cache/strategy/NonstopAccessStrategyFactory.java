package com.fantasy.framework.hibernate.cache.strategy;

import com.fantasy.framework.hibernate.cache.nonstop.HibernateNonstopCacheExceptionHandler;
import com.fantasy.framework.hibernate.cache.nonstop.NonstopAwareCollectionRegionAccessStrategy;
import com.fantasy.framework.hibernate.cache.nonstop.NonstopAwareEntityRegionAccessStrategy;
import com.fantasy.framework.hibernate.cache.nonstop.NonstopAwareNaturalIdRegionAccessStrategy;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheCollectionRegion;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;


public class NonstopAccessStrategyFactory implements SpringCacheAccessStrategyFactory {

    private final SpringCacheAccessStrategyFactory actualFactory;

    public NonstopAccessStrategyFactory(SpringCacheAccessStrategyFactory actualFactory) {
        this.actualFactory = actualFactory;
    }

    @Override
    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(SpringCacheEntityRegion entityRegion, AccessType accessType) {
        return new NonstopAwareEntityRegionAccessStrategy(actualFactory.createEntityRegionAccessStrategy(entityRegion, accessType), HibernateNonstopCacheExceptionHandler.getInstance());
    }

    @Override
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(SpringCacheNaturalIdRegion naturalIdRegion, AccessType accessType) {
        return new NonstopAwareNaturalIdRegionAccessStrategy(actualFactory.createNaturalIdRegionAccessStrategy(naturalIdRegion, accessType), HibernateNonstopCacheExceptionHandler.getInstance());
    }

    @Override
    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(SpringCacheCollectionRegion collectionRegion, AccessType accessType) {
        return new NonstopAwareCollectionRegionAccessStrategy(actualFactory.createCollectionRegionAccessStrategy(collectionRegion, accessType), HibernateNonstopCacheExceptionHandler.getInstance());
    }

}
