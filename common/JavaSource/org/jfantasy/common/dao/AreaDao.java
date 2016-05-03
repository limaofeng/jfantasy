package org.jfantasy.common.dao;

import org.hibernate.Query;
import org.jfantasy.common.bean.Area;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class AreaDao extends HibernateDao<Area, String> {

	public Integer getSortMax() {
		Query query = this.createQuery("select max(sort) from " + Area.class.getName());
		Object result = query.uniqueResult();
		return result == null ? 1 : Integer.valueOf(result.toString());
	}
}
