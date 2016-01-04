package org.jfantasy.test.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.test.bean.ArticleCategory;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleCategoryDao extends HibernateDao<ArticleCategory, String> {

}
