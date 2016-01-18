package org.jfantasy.framework.lucene.dao;

import org.jfantasy.framework.lucene.backend.EntityChangedListener;

import java.util.List;

public interface LuceneDao {

    long count();

    <T> List<T> find(int start, int size);

    /**
     *
     * @param fieldName    字段
     * @param fieldValue 字段值
     * @return List<T>
     */
    <T> List<T> findByField(String fieldName, String fieldValue);

    <T> T getById(String id);

    EntityChangedListener getLuceneListener();
}
