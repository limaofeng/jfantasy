package org.jfantasy.framework.hibernate.cache.strategy;


import org.jfantasy.framework.hibernate.cache.regions.SpringCacheCollectionRegion;
import org.hibernate.boot.spi.SessionFactoryOptions;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.internal.DefaultCacheKeysFactory;
import org.hibernate.cache.spi.CollectionRegion;
import org.hibernate.cache.spi.access.CollectionRegionAccessStrategy;
import org.hibernate.cache.spi.access.SoftLock;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.springframework.cache.Cache;

public class TransactionalSpringCacheCollectionRegionAccessStrategy extends AbstractSpringCacheAccessStrategy<SpringCacheCollectionRegion> implements CollectionRegionAccessStrategy {

    private final Cache cache;

    public TransactionalSpringCacheCollectionRegionAccessStrategy(SpringCacheCollectionRegion region, Cache cache, SessionFactoryOptions settings) {
        super(region, settings);
        this.cache = cache;
    }

    @Override
    public Object get(SessionImplementor session,Object key, long txTimestamp) throws CacheException {
        return cache.get(key);
    }

    @Override
    public CollectionRegion getRegion() {
        return region();
    }

    @Override
    public SoftLock lockItem(SessionImplementor session,Object key, Object version) throws CacheException {
        return null;
    }

    @Override
    public boolean putFromLoad(SessionImplementor session,Object key, Object value, long txTimestamp, Object version, boolean minimalPutOverride) throws CacheException {
        if (minimalPutOverride && cache.get(key) != null) {
            return false;
        }
        cache.put(key, value);
        return true;
    }

    @Override
    public void remove(SessionImplementor session,Object key) throws CacheException {
        cache.evict(key);
    }

    @Override
    public void unlockItem(SessionImplementor session,Object key, SoftLock lock) throws CacheException {
    }

    @Override
    public Object generateCacheKey(Object id, CollectionPersister persister, SessionFactoryImplementor factory, String tenantIdentifier) {
        return DefaultCacheKeysFactory.createCollectionKey(id, persister, factory, tenantIdentifier);
    }

    @Override
    public Object getCacheKeyId(Object cacheKey) {
        return DefaultCacheKeysFactory.getCollectionId(cacheKey);
    }

}