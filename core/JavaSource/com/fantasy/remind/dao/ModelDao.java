package com.fantasy.remind.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.remind.bean.Model;
import org.springframework.stereotype.Repository;

/**
 * 公告 Dao
 */

@Repository
public class ModelDao extends HibernateDao<Model,String> {
}
