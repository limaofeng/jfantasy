package com.fantasy.framework.lucene.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fantasy.framework.lucene.BuguIndex;
import com.fantasy.framework.lucene.dao.LuceneDao;

public class DaoCache {

	private static DaoCache instance = new DaoCache();
	private Map<String, LuceneDao<?>> cache;

	private DaoCache() {
		this.cache = new ConcurrentHashMap<String, LuceneDao<?>>();
	}

	public static DaoCache getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public <T> LuceneDao<T> get(Class<T> clazz) {
		LuceneDao<T> dao = null;
		String name = clazz.getName();
		if (this.cache.containsKey(name)) {
			dao = (LuceneDao<T>) this.cache.get(name);
		} else {
			dao = BuguIndex.getInstance().getLuceneDao(clazz);
			if (dao != null) {
				this.cache.put(name, dao);
			}
		}
		return dao;
	}

}
