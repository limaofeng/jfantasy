package com.fantasy.test.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.lucene.backend.EntityChangedListener;
import com.fantasy.framework.lucene.dao.LuceneDao;
import com.fantasy.test.bean.Article;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class ArticleDao extends HibernateDao<Article, Long> implements LuceneDao<Article> {

    @Override
    public Session getSession() {
        return super.getSession();
    }

    @Override
    public void changePropertyName(Criteria criteria, Set<String> alias, Criterion c) {
        super.changePropertyName(criteria, alias, c);
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public List<Article> find(int start, int size) {
        return super.find(new Criterion[0], start, size);
    }

    @Override
    public List<Article> findByField(String fieldName, String fieldValue) {
        return super.findBy(fieldName, fieldValue);
    }

    @Override
    public Article get(String id) {
        return super.get(Long.valueOf(id));
    }

    private EntityChangedListener changedListener = new EntityChangedListener(this.entityClass);

    @Override
    public EntityChangedListener getLuceneListener() {
        return changedListener;
    }
}
