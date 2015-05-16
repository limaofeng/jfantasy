package com.fantasy.framework.dao.hibernate.cache.redis.regions;

import com.fantasy.framework.dao.hibernate.cache.redis.RedisCacheMessageLogger;
import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.GeneralDataRegion;
import org.jboss.logging.Logger;
import org.springframework.cache.Cache;

import java.util.Properties;

public class RedisCacheGeneralDataRegion extends RedisCacheDataRegion implements GeneralDataRegion {

    private static final RedisCacheMessageLogger LOG = Logger.getMessageLogger(RedisCacheMessageLogger.class, RedisCacheGeneralDataRegion.class.getName());

    public RedisCacheGeneralDataRegion(RedisCacheAccessStrategyFactory accessStrategyFactory, Cache underlyingCache, Properties properties) {
        super(accessStrategyFactory, underlyingCache, properties);
    }

    @Override
    public Object get(Object key) throws CacheException {
        LOG.debugf("key: %s", key);
        if (key == null) {
            return null;
        } else {
            final Object element = getCache().get(key);
            if (element == null) {
                LOG.debugf("Element for key %s is null", key);
                return null;
            } else {
                return element;
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