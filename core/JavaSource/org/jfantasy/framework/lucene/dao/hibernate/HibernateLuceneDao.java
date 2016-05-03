package org.jfantasy.framework.lucene.dao.hibernate;

import org.hibernate.criterion.Criterion;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.lucene.backend.EntityChangedListener;
import org.jfantasy.framework.lucene.dao.LuceneDao;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.JdbcUtil;

import java.io.Serializable;
import java.util.List;

public class HibernateLuceneDao implements LuceneDao {//NOSONAR

    private HibernateDao hibernateDao;

    public HibernateLuceneDao(HibernateDao hibernateDao) {
        this.hibernateDao = hibernateDao;
        this.changedListener = new EntityChangedListener(hibernateDao.getEntityClass());
    }

    @Override
    public long count() {
        return JdbcUtil.transaction(new JdbcUtil.Callback<Long>() {
            public Long run() {
                return (long) hibernateDao.count();
            }
        });
    }

    @Override
    public <T>  List<T> find(final int start, final int size) {
        return JdbcUtil.transaction(new JdbcUtil.Callback<List<T>>() {
            @Override
            public List<T> run() {
                return hibernateDao.find(new Criterion[0], start, size);
            }
        });
    }

    @Override
    public <T> List<T> findByField(final String fieldName, final String fieldValue) {
        return JdbcUtil.transaction(new JdbcUtil.Callback<List<T>>() {
            @Override
            public List<T> run() {
                return hibernateDao.findBy(fieldName, fieldValue);
            }
        });
    }

    @Override
    public <T>  T getById(final String id) {
        return JdbcUtil.transaction(new JdbcUtil.Callback<T>() {
            @Override
            public T run() {
                return (T) hibernateDao.get((Serializable) ClassUtil.newInstance(ReflectionUtils.getSuperClassGenricType(hibernateDao.getClass(), 1) , new Class[]{String.class}, new Object[]{id}));
            }
        });
    }

    private EntityChangedListener changedListener;

    @Override
    public EntityChangedListener getLuceneListener() {
        return changedListener;
    }

}
