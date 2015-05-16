package com.fantasy.framework.dao.hibernate.cache;

import org.hibernate.HibernateException;
import org.hibernate.cache.spi.QueryCache;
import org.hibernate.cache.spi.QueryCacheFactory;
import org.hibernate.cache.spi.UpdateTimestampsCache;
import org.hibernate.cfg.Settings;

import java.util.Properties;

public class CacheQueryCacheFactory implements QueryCacheFactory {

    @Override
    public QueryCache getQueryCache(
            final String regionName,
            final UpdateTimestampsCache updateTimestampsCache,
            final Settings settings,
            final Properties props) throws HibernateException {
        return new CacheQueryCache(settings, props, updateTimestampsCache, regionName);
    }

}