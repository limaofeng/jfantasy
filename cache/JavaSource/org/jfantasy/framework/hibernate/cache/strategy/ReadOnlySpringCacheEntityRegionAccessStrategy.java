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

public class ReadOnlySpringCacheEntityRegionAccessStrategy extends AbstractSpringCacheAccessStrategy<SpringCacheEntityRegion> implements EntityRegionAccessStrategy {

    public ReadOnlySpringCacheEntityRegionAccessStrategy(SpringCacheEntityRegion region, SessionFactoryOptions settings) {
        super(region, settings);
    }

    @Override
    public EntityRegion getRegion() {
        return region();
    }

    @Override
    public Object get(SessionImplementor session, Object key, long txTimestamp) throws CacheException {
        return region().get(key);
    }

    @Override
    public boolean putFromLoad(SessionImplementor session, Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride)
            throws CacheException {
        if (minimalPutOverride && region().contains(key)) {
            return false;
        } else {
            region().put(key, value);
            return true;
        }
    }

    @Override
    public SoftLock lockItem(SessionImplementor session, Object key, Object version) throws UnsupportedOperationException {
        return null;
    }

    @Override
    public void unlockItem(SessionImplementor session, Object key, SoftLock lock) throws CacheException {
        evict(key);
    }

    @Override
    public boolean insert(SessionImplementor session, Object key, Object value, Object version) throws CacheException {
        return false;
    }

    @Override
    public boolean afterInsert(SessionImplementor session, Object key, Object value, Object version) throws CacheException {
        region().put(key, value);
        return true;
    }

    @Override
    public boolean update(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Can't write to a readonly object");
    }

    @Override
    public boolean afterUpdate(SessionImplementor session, Object key, Object value, Object currentVersion, Object previousVersion, SoftLock lock) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Can't write to a readonly object");
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
