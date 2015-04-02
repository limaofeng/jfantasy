package com.fantasy.framework.lucene.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.lucene.BuguSearcher;
import com.fantasy.framework.lucene.bean.News;
import com.fantasy.framework.lucene.dao.NewsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional
public class NewsService extends BuguSearcher<News>{

    @Autowired
    private NewsDao newsDao;

    public void save(News news){
        newsDao.save(news);
    }

    public void deleteAll(){
        Pager<News> pager = this.newsDao.findPager(new Pager<News>(),new ArrayList<PropertyFilter>());
        while (!pager.getPageItems().isEmpty()){
            for(News news : pager.getPageItems()) {
                newsDao.delete(news);
            }
            pager.setCurrentPage(pager.getCurrentPage()+1);
            pager = this.newsDao.findPager(pager,new ArrayList<PropertyFilter>());
        }
    }

}
