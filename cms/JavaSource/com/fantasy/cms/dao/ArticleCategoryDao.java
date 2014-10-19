package com.fantasy.cms.dao;

import org.springframework.stereotype.Repository;

import com.fantasy.cms.bean.ArticleCategory;
import com.fantasy.framework.dao.hibernate.HibernateDao;

@Repository("fantasy.cms.hibernate.ArticleCategoryDao")
public class ArticleCategoryDao extends HibernateDao<ArticleCategory, String> {
	
}
