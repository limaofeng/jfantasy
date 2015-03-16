package com.fantasy.attr.storage.dao;

import com.fantasy.attr.storage.bean.Article;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public class ArticleDao extends HibernateDao<Article,Long>{

    @Override
    public Session getSession() {
        return super.getSession();
    }

    @Override
    public void changePropertyName(Criteria criteria, Set<String> alias, Criterion c) {
        super.changePropertyName(criteria, alias, c);
    }
}
