package com.fantasy.framework.cache;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Resource;

import com.fantasy.framework.util.common.ObjectUtil;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class DefaultEhCache implements Cache {

	@Resource(name = "ehcache")
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
		try {
			Element cachedElement = getCache().get(Integer.valueOf(key.hashCode()));
			if (cachedElement == null) {
				return null;
			}
			return cachedElement.getObjectValue();
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	public ReadWriteLock getReadWriteLock() {
		return this.readWriteLock;
	}

	public int getSize() {
		try {
			return getCache().getSize();
		} catch (Throwable t) {
			throw new CacheException(t);
		}

	}

	public void putObject(Object key, Object value) {
		try {
			getCache().put(new Element(Integer.valueOf(key.hashCode()), value));
		} catch (Throwable t) {
			throw new CacheException(t);
		}
	}

	public Object removeObject(Object key) {
		try {
			Object obj = getObject(key);
			getCache().remove(Integer.valueOf(key.hashCode()));
			return obj;
		} catch (Throwable t) {
			throw new CacheException(t);
		}

	}

	private Ehcache getCache() {
		Ehcache cache = this.cacheManager.getCache(this.id);
		if (ObjectUtil.isNull(cache))
			this.cacheManager.addCache(this.id);
		return this.cacheManager.getCache(this.id);
	}

	public String toString() {
		return "EHCache {" + this.id + "}";
	}
}