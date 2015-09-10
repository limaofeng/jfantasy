package com.fantasy.framework.lucene.dao.hibernate;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.util.ReflectionUtils;
import com.fantasy.framework.lucene.backend.EntityChangedListener;
import com.fantasy.framework.lucene.dao.LuceneDao;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.JdbcUtil;
import org.hibernate.criterion.Criterion;

import java.io.Serializable;
import java.util.List;

public abstract class HibernateLuceneDao<T, PK extends Serializable> extends HibernateDao<T, PK> implements LuceneDao<T> {//NOSONAR

    @Override
    public long count() {
        return JdbcUtil.transaction(new JdbcUtil.Callback<Long>() {
            public Long run() {
                return (long) HibernateLuceneDao.super.count();
            }
        });
    }

    @Override
    public List<T> find(final int start, final int size) {
        return JdbcUtil.transaction(new JdbcUtil.Callback<List<T>>() {
            @Override
            public List<T> run() {
                return HibernateLuceneDao.super.find(new Criterion[0], start, size);
            }
        });
    }

    @Override
    public List<T> findByField(final String fieldName, final String fieldValue) {
        return JdbcUtil.transaction(new JdbcUtil.Callback<List<T>>() {
            @Override
            public List<T> run() {
                return HibernateLuceneDao.super.findBy(fieldName, fieldValue);
            }
        });
    }

    @Override
    public T getById(final String id) {
        return JdbcUtil.transaction(new JdbcUtil.Callback<T>() {
            @Override
            public T run() {
                return HibernateLuceneDao.super.get((PK) ClassUtil.newInstance(ReflectionUtils.getSuperClassGenricType(HibernateLuceneDao.this.getClass(), 1), new Class[]{String.class}, new Object[]{id}));
            }
        });
    }

    private EntityChangedListener changedListener = new EntityChangedListener(super.entityClass);

    @Override
    public EntityChangedListener getLuceneListener() {
        return changedListener;
    }


}
