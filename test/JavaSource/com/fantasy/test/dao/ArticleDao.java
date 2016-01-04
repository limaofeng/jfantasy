package org.jfantasy.test.dao;

import org.jfantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import org.jfantasy.test.bean.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao extends HibernateLuceneDao<Article, Long> {

}
