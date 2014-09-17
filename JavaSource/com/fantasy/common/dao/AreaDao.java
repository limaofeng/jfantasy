package com.fantasy.common.dao;

import com.fantasy.common.bean.Area;
import com.fantasy.framework.dao.hibernate.HibernateDao;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository
public class AreaDao extends HibernateDao<Area, String> {

	public Integer getSortMax() {
		Query query = this.createQuery("select max(sort) from " + Area.class.getName());
		Object result = query.uniqueResult();
		return result == null ? 1 : Integer.valueOf(result.toString());
	}
}
