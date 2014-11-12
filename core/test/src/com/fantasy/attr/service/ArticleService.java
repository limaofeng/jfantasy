package com.fantasy.attr.service;

import com.fantasy.attr.bean.Article;
import com.fantasy.attr.dao.ArticleDao;
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
        for(Long id : ids) {
            this.articleDao.delete(id);
        }
    }

    public Pager<Article> findPager(Pager<Article> pager,List<PropertyFilter> filters) {
        return this.articleDao.findPager(pager,filters);
    }

}
