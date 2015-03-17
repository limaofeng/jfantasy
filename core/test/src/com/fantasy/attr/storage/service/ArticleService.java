package com.fantasy.attr.storage.service;

import com.fantasy.attr.storage.bean.Article;
import com.fantasy.attr.storage.dao.ArticleDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ArticleService {

    @Autowired
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
    public List<Article> find(List<PropertyFilter> filters,String orderby, String order, int size) {
        return this.articleDao.find(filters,orderby,order,0,size);
    }
}
