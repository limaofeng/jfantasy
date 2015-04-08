package com.fantasy.framework.lucene.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.lucene.backend.EntityChangedListener;
import com.fantasy.framework.lucene.bean.News;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NewsDao extends HibernateDao<News,Long> implements LuceneDao<News> {

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public List<News> find(int start, int size) {
        return super.find(new Criterion[0],start,size);
    }

    @Override
    public List<News> findByField(String fieldName, String fieldValue) {
        return super.findBy(fieldName,fieldValue);
    }

    @Override
    public News get(String id) {
        return super.get(Long.valueOf(id));
    }

    private EntityChangedListener changedListener = new EntityChangedListener(this.entityClass);

    @Override
    public EntityChangedListener getLuceneListener() {
        return changedListener;
    }
}
