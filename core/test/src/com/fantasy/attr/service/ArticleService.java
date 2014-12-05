package com.fantasy.attr.service;

import com.fantasy.attr.bean.Article;
import com.fantasy.attr.dao.ArticleDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.loader.criteria.CriteriaJoinWalker;
import org.hibernate.loader.criteria.CriteriaQueryTranslator;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class ArticleService {

    @Resource
    private ArticleDao articleDao;

    public void save(Article article) {
        this.articleDao.save(article);
    }

    public Article get(Long id) {
        return this.articleDao.get(id);
    }

    public List<Article> find(Criterion... criterions) {
        return this.articleDao.find(criterions);
    }

    public void delete(Long... ids) {
        for (Long id : ids) {
            this.articleDao.delete(id);
        }
    }

    public Pager<Article> findPager(Pager<Article> pager, List<PropertyFilter> filters) {
        return this.articleDao.findPager(pager, filters);
    }

    public Article findUnique(Criterion... criterions) {
        return this.articleDao.findUnique(criterions);
    }

    public Article findUniqueBy(String propertyName, Object value) {
        return this.articleDao.findUniqueBy(propertyName, value);
    }

    public List<Article> find(List<PropertyFilter> filters) {
        return this.articleDao.find(filters,"id","asc",0,10);
    }
}
