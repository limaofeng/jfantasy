package com.fantasy.question.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.question.bean.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends HibernateDao<Category,Long> {
}
