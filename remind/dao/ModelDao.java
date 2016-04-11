package org.jfantasy.remind.dao;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.remind.bean.Model;
import org.springframework.stereotype.Repository;

/**
 * 公告 Dao
 */

@Repository
public class ModelDao extends HibernateDao<Model,String> {
}
