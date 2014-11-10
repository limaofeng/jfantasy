package com.fantasy.attr.service;

import com.fantasy.attr.bean.Article;
import com.fantasy.attr.dao.ArticleDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ArticleService {

    @Resource
    private ArticleDao articleDao;

    public void save(Article article) {
        this.articleDao.save(article);
    }

    public Article get(Long id) {
        return this.articleDao.get(id);
    }

}
