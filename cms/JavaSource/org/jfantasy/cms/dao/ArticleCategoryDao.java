package org.jfantasy.cms.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.cms.bean.ArticleCategory;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository("fantasy.cms.hibernate.ArticleCategoryDao")
public class ArticleCategoryDao extends HibernateDao<ArticleCategory, String> {
	
}
