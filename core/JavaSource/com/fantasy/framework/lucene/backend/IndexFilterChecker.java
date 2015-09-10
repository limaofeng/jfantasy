package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.annotations.Compare;
import com.fantasy.framework.lucene.annotations.IndexFilter;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.util.reflect.Property;

public class IndexFilterChecker {

    private Object entity;

    public IndexFilterChecker(Object entity) {
        this.entity = entity;
    }

    public boolean needIndex() {
        Class<?> clazz = this.entity.getClass();
        for (Property p : PropertysCache.getInstance().filter(clazz, IndexFilter.class)) {
            IndexFilter filter = p.getAnnotation(IndexFilter.class);
            Compare compare = filter.compare();
            String value = filter.value();
            CompareChecker checker = new CompareChecker(this.entity);
            if (!checker.isFit(p, compare, value)) {
                return false;
            }
        }
        return true;
    }

}
