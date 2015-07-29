package com.fantasy.framework.lucene.backend;

import java.lang.reflect.Field;

import com.fantasy.framework.lucene.annotations.Compare;
import com.fantasy.framework.lucene.annotations.IndexFilter;
import com.fantasy.framework.lucene.cache.FieldsCache;

public class IndexFilterChecker {

    private Object entity;

    public IndexFilterChecker(Object entity) {
        this.entity = entity;
    }

    public boolean needIndex() {
        Class<?> clazz = this.entity.getClass();
        Field[] fields = FieldsCache.getInstance().get(clazz);
        for (Field f : fields) {
            IndexFilter filter = (IndexFilter) f.getAnnotation(IndexFilter.class);
            if (filter != null) {
                Compare compare = filter.compare();
                String value = filter.value();
                CompareChecker checker = new CompareChecker(this.entity);
                if (!checker.isFit(f, compare, value)) {
                    return false;
                }
            }
        }
        return true;
    }

}
