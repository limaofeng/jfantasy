package com.fantasy.framework.lucene.dao;

import com.fantasy.framework.lucene.backend.EntityChangedListener;

import java.util.List;

public interface LuceneDao<T> {

    long count();

    List<T> find(int start, int size);

    /**
     * 不完善 待调整
     *
     * @param fieldName    字段
     * @param fieldIdValue 字段值
     * @return List<T>
     */
    List<T> findByField(String fieldName, String fieldIdValue);

    List<T> findByIds(String... ids);

    T get(String id);

    EntityChangedListener getLuceneListener();
}
