package com.fantasy.framework.hibernate.cache.strategy;


import com.fantasy.framework.hibernate.cache.regions.SpringCacheCollectionRegion;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cfg.Settings;

public class ReadWriteSpringCacheCollectionRegionAccessStrategy extends AbstractReadWriteSpringCacheAccessStrategy<SpringCacheCollectionRegion> implements CollectionRegionAccessStrategy {

    public ReadWriteSpringCacheCollectionRegionAccessStrategy(SpringCacheCollectionRegion region, Settings settings) {
        super(region, settings);
    }

    @Override
    public CollectionRegion getRegion() {
        return region();
    }

}
