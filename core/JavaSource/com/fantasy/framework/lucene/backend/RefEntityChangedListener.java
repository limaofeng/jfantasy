package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.BuguIndex;
import com.fantasy.framework.lucene.annotations.IndexRef;
import com.fantasy.framework.lucene.annotations.IndexRefList;
import com.fantasy.framework.lucene.cache.DaoCache;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.lucene.dao.LuceneDao;
import com.fantasy.framework.util.common.JdbcUtil;
import com.fantasy.framework.util.reflect.Property;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class RefEntityChangedListener {
    private Set<Class<?>> refBySet;

    public RefEntityChangedListener(Set<Class<?>> refBySet) {
        this.refBySet = refBySet;
    }

    public void entityChange(Class<?> refClass, String id) {
        for (Class<?> cls : this.refBySet) {
            for (Property p : PropertysCache.getInstance().get(cls)) {
                processField(refClass, id, cls, p);
            }
        }
    }

    private void processField(Class<?> refClass, String id, Class<?> cls, Property p) {
        boolean match = false;
        String fieldName = p.getName();
        if (p.getPropertyType().equals(refClass) && (p.getAnnotation(IndexRef.class) != null)) {
            match = true;
        } else {
            if (p.getAnnotation(IndexRefList.class) != null) {
                Class<?> c;
                Class<?> type = p.getPropertyType();
                if (type.isArray()) {
                    c = type.getComponentType();
                } else {
                    ParameterizedType paramType = p.getGenericType();
                    Type[] types = paramType.getActualTypeArguments();
                    if (types.length == 1) {
                        c = (Class<?>) types[0];
                    } else {
                        c = (Class<?>) types[1];
                    }
                }
                if ((c != null) && (c.equals(refClass))) {
                    match = true;
                }
            }
        }
        if (match) {
            LuceneDao<?> dao = DaoCache.getInstance().get(cls);
            if (dao != null) {
                JdbcUtil.Transaction transaction = JdbcUtil.transaction();
                try {
                    List<?> list = dao.findByField(fieldName, id);
                    for (Object o : list) {
                        IndexUpdateTask task = new IndexUpdateTask(o);
                        BuguIndex.getInstance().getExecutor().execute(task);
                    }
                } finally {
                    transaction.commit();
                }
            }
        }

    }
}