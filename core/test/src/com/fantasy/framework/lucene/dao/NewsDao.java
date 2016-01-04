package org.jfantasy.framework.lucene.dao;

import org.jfantasy.framework.lucene.bean.News;
import org.jfantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import org.springframework.stereotype.Repository;

@Repository
public class NewsDao extends HibernateLuceneDao<News,Long> {

}
