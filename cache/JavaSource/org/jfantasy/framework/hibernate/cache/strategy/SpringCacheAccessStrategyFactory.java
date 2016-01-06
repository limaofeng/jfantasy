package org.jfantasy.framework.hibernate.cache.strategy;

import org.jfantasy.framework.hibernate.cache.regions.SpringCacheCollectionRegion;
import org.jfantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import org.jfantasy.framework.hibernate.cache.regions.SpringCacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;

public interface SpringCacheAccessStrategyFactory {

    EntityRegionAccessStrategy createEntityRegionAccessStrategy(SpringCacheEntityRegion entityRegion, AccessType accessType);

    CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(SpringCacheCollectionRegion collectionRegion, AccessType accessType);

    NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(SpringCacheNaturalIdRegion naturalIdRegion, AccessType accessType);

}
