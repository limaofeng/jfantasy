package com.fantasy.framework.dao.hibernate.cache.redis;

import com.fantasy.framework.dao.hibernate.cache.redis.regions.*;
import com.fantasy.framework.dao.hibernate.cache.redis.strategy.NonstopAccessStrategyFactory;
import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactory;
import com.fantasy.framework.dao.hibernate.cache.redis.strategy.RedisCacheAccessStrategyFactoryImpl;
import com.fantasy.framework.dao.hibernate.cache.redis.util.Timestamper;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.ehcache.management.impl.ProviderMBeanRegistrationHelper;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;

import java.util.Properties;

abstract class AbstractRedisRegionFactory implements RegionFactory {

    protected final ProviderMBeanRegistrationHelper mbeanRegistrationHelper = new ProviderMBeanRegistrationHelper();

    protected volatile RedisCacheManager manager;

    protected Settings settings;

    protected final RedisCacheAccessStrategyFactory accessStrategyFactory = new NonstopAccessStrategyFactory(new RedisCacheAccessStrategyFactoryImpl());

    @Override
    public boolean isMinimalPutsEnabledByDefault() {
        return true;
    }

    @Override
    public long nextTimestamp() {
        return Timestamper.next();
    }

    @Override
    public EntityRegion buildEntityRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new RedisCacheEntityRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new RedisCacheNaturalIdRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new RedisCacheCollectionRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new RedisCacheQueryResultsRegion(accessStrategyFactory, getCache(regionName), properties);
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new RedisCacheTimestampsRegion(accessStrategyFactory, getCache(regionName), properties);
    }

    private Cache getCache(String name) throws CacheException {
        return manager.getCache(name);
    }

    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

}

