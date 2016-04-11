package org.jfantasy.springboot.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.springboot.bean.Article;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao extends HibernateDao<Article,Long> {
}
