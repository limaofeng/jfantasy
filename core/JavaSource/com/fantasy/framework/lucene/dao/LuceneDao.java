package com.fantasy.framework.lucene.dao;

import com.fantasy.framework.lucene.backend.EntityChangedListener;

import java.util.List;

public interface LuceneDao<T> {

    long count();

    List<T> find(int start, int size);

    /**
     *
     * @param fieldName    字段
     * @param fieldValue 字段值
     * @return List<T>
     */
    List<T> findByField(String fieldName, String fieldValue);

    T get(String id);

    EntityChangedListener getLuceneListener();
}
