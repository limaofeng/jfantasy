package org.jfantasy.question.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.question.bean.Category;
import org.springframework.stereotype.Repository;

@Repository
public class CategoryDao extends HibernateDao<Category,Long> {
}
