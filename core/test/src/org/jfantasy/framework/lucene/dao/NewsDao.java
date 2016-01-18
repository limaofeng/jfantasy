package org.jfantasy.framework.lucene.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.lucene.bean.News;
import org.springframework.stereotype.Repository;

@Repository
public class NewsDao extends HibernateDao<News,Long> {

}
