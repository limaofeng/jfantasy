package com.fantasy.framework.hibernate.cache.strategy;

import com.fantasy.framework.hibernate.cache.regions.SpringCacheCollectionRegion;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

public interface SpringCacheAccessStrategyFactory {

    EntityRegionAccessStrategy createEntityRegionAccessStrategy(SpringCacheEntityRegion entityRegion, AccessType accessType);

    CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(SpringCacheCollectionRegion collectionRegion, AccessType accessType);

    NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(SpringCacheNaturalIdRegion naturalIdRegion, AccessType accessType);

}
