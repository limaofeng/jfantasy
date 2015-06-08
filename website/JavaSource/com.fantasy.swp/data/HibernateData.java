package com.fantasy.swp.data;

import com.fantasy.framework.spring.SpringContextUtil;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import java.util.List;

public class HibernateData extends AbstractTemplateData {

	private static HibernateDaoSupport query;

	private String resultType;
	private String hql;

	@SuppressWarnings("unchecked")
	public Object getValue() {
		List<Object> list = (List<Object>) getQuery().getHibernateTemplate().find(this.hql);
		if ("list".equals(resultType)) {
			return list;
		}
		return list.isEmpty() ? null : list.get(0);
	}

	public static HibernateDaoSupport getQuery() {
		if (query == null) {
			query = SpringContextUtil.createBean(Query.class, SpringContextUtil.AUTOWIRE_BY_TYPE);
		}
		return query;
	}

	public void setHql(String hql) {
		this.hql = hql;
	}

	public void setResultType(String resultType) {
		this.resultType = resultType;
	}

	public static class Query extends HibernateDaoSupport {

	}

}
