package org.jfantasy.framework.lucene.dao.hibernate;

import org.hibernate.criterion.Criterion;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.lucene.backend.EntityChangedListener;
import org.jfantasy.framework.lucene.dao.LuceneDao;
import org.jfantasy.framework.util.common.ClassUtil;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public class HibernateLuceneDao implements LuceneDao {//NOSONAR

    private HibernateDao hibernateDao;

    public HibernateLuceneDao(HibernateDao hibernateDao) {
        this.hibernateDao = hibernateDao;
        this.changedListener = new EntityChangedListener(hibernateDao.getEntityClass());
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    public long count() {
        return hibernateDao.count();
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public <T> List<T> find(final int start, final int size) {
        return hibernateDao.find(new Criterion[0], start, size);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public <T> List<T> findByField(final String fieldName, final String fieldValue) {
        return hibernateDao.findBy(fieldName, fieldValue);
    }

    @Override
    @Transactional(readOnly = true, propagation = Propagation.NOT_SUPPORTED)
    @SuppressWarnings("unchecked")
    public <T> T getById(final String id) {
        return (T) hibernateDao.get((Serializable) ClassUtil.newInstance(hibernateDao.getIdClass(), new Class[]{String.class}, new Object[]{id}));
    }

    private EntityChangedListener changedListener;

    @Override
    public EntityChangedListener getLuceneListener() {
        return changedListener;
    }

}
