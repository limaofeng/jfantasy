package com.fantasy.test.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.test.bean.ArticleCategory;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleCategoryDao extends HibernateDao<ArticleCategory, String> {

}
