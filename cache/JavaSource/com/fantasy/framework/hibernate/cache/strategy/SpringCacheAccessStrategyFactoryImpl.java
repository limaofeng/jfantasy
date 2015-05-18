package com.fantasy.framework.hibernate.cache.strategy;


import com.fantasy.framework.hibernate.cache.log.SpringCacheMessageLogger;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheCollectionRegion;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import com.fantasy.framework.hibernate.cache.regions.SpringCacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.jboss.logging.Logger;

public class SpringCacheAccessStrategyFactoryImpl implements SpringCacheAccessStrategyFactory {

    private static final SpringCacheMessageLogger LOG = Logger.getMessageLogger(SpringCacheMessageLogger.class, SpringCacheAccessStrategyFactoryImpl.class.getName());

    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(SpringCacheEntityRegion entityRegion, AccessType accessType) {
        switch (accessType) {
            case READ_ONLY:
                if (entityRegion.getCacheDataDescription().isMutable()) {
                    LOG.readOnlyCacheConfiguredForMutableEntity(entityRegion.getName());
                }
                return new ReadOnlySpringCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
            case READ_WRITE:
                return new ReadWriteSpringCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteSpringCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
            case TRANSACTIONAL:
                return new TransactionalSpringCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSpringCache(), entityRegion.getSettings());
            default:
                throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");

        }

    }

    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(SpringCacheCollectionRegion collectionRegion, AccessType accessType) {
        switch (accessType) {
            case READ_ONLY:
                if (collectionRegion.getCacheDataDescription().isMutable()) {
                    LOG.readOnlyCacheConfiguredForMutableEntity(collectionRegion.getName());
                }
                return new ReadOnlySpringCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSettings());
            case READ_WRITE:
                return new ReadWriteSpringCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSettings());
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteSpringCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSettings());
            case TRANSACTIONAL:
                return new TransactionalSpringCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSpringCache(), collectionRegion.getSettings());
            default:
                throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }

    @Override
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(SpringCacheNaturalIdRegion naturalIdRegion, AccessType accessType) {
        switch (accessType) {
            case READ_ONLY:
                if (naturalIdRegion.getCacheDataDescription().isMutable()) {
                    LOG.readOnlyCacheConfiguredForMutableEntity(naturalIdRegion.getName());
                }
                return new ReadOnlySpringCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSettings());
            case READ_WRITE:
                return new ReadWriteSpringCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSettings());
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteSpringCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSettings());
            case TRANSACTIONAL:
                return new TransactionalSpringCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSpringCache(), naturalIdRegion.getSettings());
            default:
                throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }

}
