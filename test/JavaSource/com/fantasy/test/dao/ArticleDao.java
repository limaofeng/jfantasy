package com.fantasy.test.dao;

import com.fantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import com.fantasy.test.bean.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao extends HibernateLuceneDao<Article, Long> {

}
