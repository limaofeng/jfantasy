package org.jfantasy.framework.lucene.backend;

import org.jfantasy.framework.lucene.annotations.Compare;
import org.jfantasy.framework.lucene.annotations.IndexFilter;
import org.jfantasy.framework.lucene.cache.PropertysCache;
import org.jfantasy.framework.util.reflect.Property;

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
