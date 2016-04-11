package org.jfantasy.springboot.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.springboot.bean.Article;
import org.jfantasy.springboot.dao.ArticleDao;
import org.jfantasy.springboot.dao.ArticleJPADao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleJPADao articleJPADao;

    @Autowired
    private ArticleDao articleDao;

    @Transactional
    public Article save(Article article) {
//        return this.articleJPADao.save(article);//this.articleDao.save(article);
        return this.articleDao.save(article);
    }

    @Transactional
    public Iterable<Article> findAll() {
        return this.articleJPADao.findAll();
    }

    @Transactional
    public Pager<Article> findPager(Pager<Article> pager, List<PropertyFilter> filters) {
        return this.articleDao.findPager(pager, filters);
    }

}
