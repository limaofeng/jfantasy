package com.fantasy.framework.hibernate.cache.regions;

import com.fantasy.framework.hibernate.cache.log.SpringCacheMessageLogger;
import com.fantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.jboss.logging.Logger;
import org.springframework.cache.Cache;

import java.util.Properties;

public class SpringCacheGeneralDataRegion extends SpringCacheDataRegion implements GeneralDataRegion {

    private static final SpringCacheMessageLogger LOG = Logger.getMessageLogger(SpringCacheMessageLogger.class, SpringCacheGeneralDataRegion.class.getName());

    public SpringCacheGeneralDataRegion(SpringCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, properties);
    }

    @Override
    public Object get(Object key) throws CacheException {
        LOG.debugf("key: %s", key);
        if (key == null) {
            return null;
        } else {
            final Cache.ValueWrapper element = getCache().get(key);
            if (element == null) {
                LOG.debugf("Element for key %s is null", key);
                return null;
            } else {
                return element.get();
            }
        }
    }

    @Override
    public void put(Object key, Object value) throws CacheException {
        LOG.debugf("key: %s value: %s", key, value);
        try {
            getCache().put(key, value);
        } catch (IllegalArgumentException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void evict(Object key) throws CacheException {
        try {
            getCache().evict(key);
        } catch (ClassCastException e) {
            throw new CacheException(e);
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }

    @Override
    public void evictAll() throws CacheException {
        try {
            getCache().clear();
        } catch (IllegalStateException e) {
            throw new CacheException(e);
        }
    }
}