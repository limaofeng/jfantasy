package org.jfantasy.framework.hibernate.cache.strategy;

import org.jfantasy.framework.hibernate.cache.regions.SpringCacheEntityRegion;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.EntityRegion;
import org.hibernate.cache.spi.access.EntityRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.entity.EntityPersister;


public class ReadWriteSpringCacheEntityRegionAccessStrategy extends AbstractReadWriteSpringCacheAccessStrategy<SpringCacheEntityRegion> implements EntityRegionAccessStrategy {

    public ReadWriteSpringCacheEntityRegionAccessStrategy(SpringCacheEntityRegion region, SessionFactoryOptions settings) {
        super(region, settings);
    }

    @Override
    public EntityRegion getRegion() {
        return region();
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) throws CacheException {
        return false;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) throws CacheException {
        region().writeLock(key);
        try {
            final AbstractReadWriteSpringCacheAccessStrategy.Lockable item = (AbstractReadWriteSpringCacheAccessStrategy.Lockable) region().get(key);
            if (item == null) {
                region().put(key, new AbstractReadWriteSpringCacheAccessStrategy.Item(value, version, region().nextTimestamp()));
                return true;
            } else {
                return false;
            }
        } finally {
            region().writeUnlock(key);
        }
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion)
            throws CacheException {
        return false;
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock)
            throws CacheException {
        //what should we do with previousVersion here?
        region().writeLock(key);
        try {
            final AbstractReadWriteSpringCacheAccessStrategy.Lockable item = (AbstractReadWriteSpringCacheAccessStrategy.Lockable) region().get(key);

            if (item != null && item.isUnlockable(lock)) {
                final AbstractReadWriteSpringCacheAccessStrategy.Lock lockItem = (AbstractReadWriteSpringCacheAccessStrategy.Lock) item;
                if (lockItem.wasLockedConcurrently()) {
                    decrementLock(key, lockItem);
                    return false;
                } else {
                    region().put(key, new AbstractReadWriteSpringCacheAccessStrategy.Item(value, currentVersion, region().nextTimestamp()));
                    return true;
                }
            } else {
                handleLockExpiry(key, item);
                return false;
            }
        } finally {
            region().writeUnlock(key);
        }
    }

    @Override
    public Object generateCacheKey(Object id, EntityPersister persister, SessionFactoryImplementor factory, String tenantIdentifier) {
        return DefaultCacheKeysFactory.createEntityKey(id, persister, factory, tenantIdentifier);
    }

    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.getEntityId(cacheKey);
    }
}
