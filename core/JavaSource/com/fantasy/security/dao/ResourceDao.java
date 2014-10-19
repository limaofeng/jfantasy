package com.fantasy.security.dao;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.security.bean.Resource;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.Column;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository("fantasy.auth.hibernate.ResourceDao")
public class ResourceDao extends HibernateDao<Resource, Long> {

	@SuppressWarnings("unchecked")
	public Map<Long, List<Long>> getRelations() {
		Map<Long, List<Long>> res = new HashMap<Long, List<Long>>();
		SQLQuery query = this.createSQLQuery("select t.RESOURCE_ID as RESOURCE_ID,t.SUB_RESOURCE_ID as SUB_RESOURCE_ID from auth_relation_resource t");
		query = distinct(query,ResourceRelation.class);
		List<ResourceRelation> list = query.list();
		for (ResourceRelation r : list) {
			if (ObjectUtil.isNull(r.getResourceId()) || ObjectUtil.isNull(r.getSubResourceId()))
				continue;
			if (!res.containsKey(r.getResourceId())) {
				res.put(r.getResourceId(), new ArrayList<Long>());
			}
			List<Long> subResourceIds = res.get(r.getResourceId());
			subResourceIds.add(r.getSubResourceId());
		}
		return res;
	}

	public static class ResourceRelation {

		@Column(name = "RESOURCE_ID")
		private Long resourceId;
		@Column(name = "SUB_RESOURCE_ID")
		private Long subResourceId;

		public Long getResourceId() {
			return resourceId;
		}

		public void setResourceId(Long resourceId) {
			this.resourceId = resourceId;
		}

		public Long getSubResourceId() {
			return subResourceId;
		}

		public void setSubResourceId(Long subResourceId) {
			this.subResourceId = subResourceId;
		}

	}

}