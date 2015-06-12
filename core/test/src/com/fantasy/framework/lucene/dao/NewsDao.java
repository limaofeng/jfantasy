package com.fantasy.framework.lucene.dao;

import com.fantasy.framework.lucene.bean.News;
import com.fantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import org.springframework.stereotype.Repository;

@Repository
public class NewsDao extends HibernateLuceneDao<News,Long> {

}
