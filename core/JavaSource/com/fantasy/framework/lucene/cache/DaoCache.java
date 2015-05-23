package com.fantasy.framework.lucene.cache;

import com.fantasy.framework.lucene.dao.LuceneDao;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DaoCache {

    private static DaoCache instance = new DaoCache();
    private Map<Class<?>, LuceneDao<?>> cache;

    private DaoCache() {
        this.cache = new ConcurrentHashMap<Class<?>, LuceneDao<?>>();
    }

    public static DaoCache getInstance() {
        return instance;
    }

    public boolean containsKey(Class<?> clazz){
        return this.cache.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> LuceneDao<T> get(Class<T> clazz) {
       return (LuceneDao<T>)this.cache.get(clazz);
    }

    public <T> void put(Class<T> clazz, LuceneDao<T> dao) {
        this.cache.put(clazz, dao);
    }

}
