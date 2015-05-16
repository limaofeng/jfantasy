package com.fantasy.framework.dao.hibernate.cache.redis.strategy;


import com.fantasy.framework.dao.hibernate.cache.redis.RedisCacheMessageLogger;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheCollectionRegion;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheEntityRegion;
import com.fantasy.framework.dao.hibernate.cache.redis.regions.RedisCacheNaturalIdRegion;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.NaturalIdRegionAccessStrategy;
import org.jboss.logging.Logger;

public class RedisCacheAccessStrategyFactoryImpl implements RedisCacheAccessStrategyFactory {

    private static final RedisCacheMessageLogger LOG = Logger.getMessageLogger(RedisCacheMessageLogger.class, RedisCacheAccessStrategyFactoryImpl.class.getName());

    public EntityRegionAccessStrategy createEntityRegionAccessStrategy(
            RedisCacheEntityRegion entityRegion,
            AccessType accessType) {
        switch (accessType) {
            case READ_ONLY:
                if (entityRegion.getCacheDataDescription().isMutable()) {
                    LOG.readOnlyCacheConfiguredForMutableEntity(entityRegion.getName());
                }
                return new ReadOnlyRedisCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
            case READ_WRITE:
                return new ReadWriteRedisCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteRedisCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getSettings());
            case TRANSACTIONAL:
                return new TransactionalRedisCacheEntityRegionAccessStrategy(entityRegion, entityRegion.getRedisCache(), entityRegion.getSettings());
            default:
                throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");

        }

    }

    public CollectionRegionAccessStrategy createCollectionRegionAccessStrategy(RedisCacheCollectionRegion collectionRegion, AccessType accessType) {
        switch (accessType) {
            case READ_ONLY:
                if (collectionRegion.getCacheDataDescription().isMutable()) {
                    LOG.readOnlyCacheConfiguredForMutableEntity(collectionRegion.getName());
                }
                return new ReadOnlyRedisCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSettings());
            case READ_WRITE:
                return new ReadWriteRedisCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSettings());
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteRedisCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getSettings());
            case TRANSACTIONAL:
                return new TransactionalRedisCacheCollectionRegionAccessStrategy(collectionRegion, collectionRegion.getRedisCache(), collectionRegion.getSettings());
            default:
                throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }

    @Override
    public NaturalIdRegionAccessStrategy createNaturalIdRegionAccessStrategy(RedisCacheNaturalIdRegion naturalIdRegion, AccessType accessType) {
        switch (accessType) {
            case READ_ONLY:
                if (naturalIdRegion.getCacheDataDescription().isMutable()) {
                    LOG.readOnlyCacheConfiguredForMutableEntity(naturalIdRegion.getName());
                }
                return new ReadOnlyRedisCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSettings());
            case READ_WRITE:
                return new ReadWriteRedisCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSettings());
            case NONSTRICT_READ_WRITE:
                return new NonStrictReadWriteRedisCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getSettings());
            case TRANSACTIONAL:
                return new TransactionalRedisCacheNaturalIdRegionAccessStrategy(naturalIdRegion, naturalIdRegion.getRedisCache(), naturalIdRegion.getSettings());
            default:
                throw new IllegalArgumentException("unrecognized access strategy type [" + accessType + "]");
        }
    }

}
