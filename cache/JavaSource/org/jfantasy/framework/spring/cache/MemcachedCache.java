package org.jfantasy.framework.spring.cache;

import net.spy.memcached.MemcachedClient;
import org.springframework.cache.Cache;

public class MemcachedCache implements Cache {

    private final String name;

    public MemcachedCache(String name, int expire, MemcachedClient memcachedClient) {
        this.name = name;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        return null;
    }

    @Override
    public void put(Object key, Object value) {

    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}
