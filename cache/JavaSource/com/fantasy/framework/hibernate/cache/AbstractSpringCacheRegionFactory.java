package com.fantasy.framework.hibernate.cache;

import com.fantasy.framework.hibernate.cache.regions.*;
import com.fantasy.framework.hibernate.cache.strategy.NonstopAccessStrategyFactory;
import com.fantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactory;
import com.fantasy.framework.hibernate.cache.strategy.SpringCacheAccessStrategyFactoryImpl;
import com.fantasy.framework.hibernate.cache.util.Timestamper;
import org.hibernate.cache.CacheException;
import org.hibernate.cache.spi.*;
import org.hibernate.cache.spi.access.AccessType;
import org.hibernate.cfg.Settings;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Properties;

abstract class AbstractSpringCacheRegionFactory implements RegionFactory {

    protected volatile CacheManager manager;

    protected Settings settings;

    protected final SpringCacheAccessStrategyFactory accessStrategyFactory = new NonstopAccessStrategyFactory(new SpringCacheAccessStrategyFactoryImpl());

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
        return new SpringCacheEntityRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public NaturalIdRegion buildNaturalIdRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new SpringCacheNaturalIdRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public CollectionRegion buildCollectionRegion(String regionName, Properties properties, CacheDataDescription metadata) throws CacheException {
        return new SpringCacheCollectionRegion(accessStrategyFactory, getCache(regionName), settings, metadata, properties);
    }

    @Override
    public QueryResultsRegion buildQueryResultsRegion(String regionName, Properties properties) throws CacheException {
        return new SpringCacheQueryResultsRegion(accessStrategyFactory, getCache(regionName), properties);
    }

    @Override
    public TimestampsRegion buildTimestampsRegion(String regionName, Properties properties) throws CacheException {
        return new SpringCacheTimestampsRegion(accessStrategyFactory, getCache(regionName), properties);
    }

    private Cache getCache(String name) throws CacheException {
        return manager.getCache(name);
    }

    public AccessType getDefaultAccessType() {
        return AccessType.READ_WRITE;
    }

}

