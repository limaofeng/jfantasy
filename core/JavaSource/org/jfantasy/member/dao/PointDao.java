package org.jfantasy.member.dao;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.springframework.stereotype.Repository;

import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.member.bean.Point;

@Repository("PointDao")
public class PointDao extends HibernateDao<Point, Long>{

	/**
	 * 统计积分收支明细
	 * @param filters
	 * @return
	 */
	public Map<String,Integer> calculateScore(List<PropertyFilter> filters){
		Map<String,Integer> map = new HashMap<String, Integer>();
		Criterion[] criterions = buildPropertyFilterCriterions(filters);
		Criteria c = createCriteria(criterions);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("status"),"status");
		projectionList.add(Projections.sum("score"), "score");
		
		c.setProjection(projectionList);
		
		for(Object o : c.list()){
			map.put(Array.get(o, 0).toString(), (Integer)Array.get(o, 1));
		}
		
		return map;
	}
	
	/**
	 * 统计即将过期的积分
	 * @param filters
	 * @return
	 */
	public Map<String, Integer> calculateWillExpireScore(List<PropertyFilter> filters) {
		Map<String,Integer> map = new HashMap<String, Integer>();
		Criterion[] criterions = buildPropertyFilterCriterions(filters);
		Criteria c = createCriteria(criterions);
		
		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("status"),"status");
		projectionList.add(Projections.sum("score"), "score");
		
		c.setProjection(projectionList);
		
		for(Object o : c.list()){
			map.put(Array.get(o, 0).toString(), (Integer)Array.get(o, 1));
		}
		
		return map;
	}
	
}
