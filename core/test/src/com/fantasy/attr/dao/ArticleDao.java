package com.fantasy.attr.dao;

import com.fantasy.attr.bean.Article;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleDao extends HibernateDao<Article,Long>{
}
