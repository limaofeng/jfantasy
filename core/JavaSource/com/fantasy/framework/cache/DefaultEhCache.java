package com.fantasy.framework.cache;

import com.fantasy.framework.util.common.ObjectUtil;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DefaultEhCache implements Cache {

    @Autowired(name = "ehcache")
    protected CacheManager cacheManager;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final String id;

    public DefaultEhCache(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        this.id = id;
    }

    public void clear() {
        getCache().removeAll();
    }

    public String getId() {
        return this.id;
    }

    public Object getObject(Object key) {
        Element cachedElement = getCache().get(Integer.valueOf(key.hashCode()));
        if (cachedElement == null) {
            return null;
        }
        return cachedElement.getObjectValue();
    }

    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    public int getSize() {
        return getCache().getSize();
    }

    public void putObject(Object key, Object value) {
        getCache().put(new Element(Integer.valueOf(key.hashCode()), value));
    }

    public Object removeObject(Object key) {
        Object obj = getObject(key);
        getCache().remove(Integer.valueOf(key.hashCode()));
        return obj;
    }

    private Ehcache getCache() {
        Ehcache cache = this.cacheManager.getCache(this.id);
        if (ObjectUtil.isNull(cache)) {
            this.cacheManager.addCache(this.id);
        }
        return this.cacheManager.getCache(this.id);
    }

    public String toString() {
        return "EHCache {" + this.id + "}";
    }
}