package org.jfantasy.mall.sales.dao;

import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.jfantasy.common.bean.enums.TimeUnit;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.AliasToBeanResultTransformer;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.mall.goods.dao.GoodsDao;
import org.jfantasy.mall.order.dao.OrderDao;
import org.jfantasy.mall.sales.bean.Sales;
import org.jfantasy.mall.sales.bean.Sales.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class SalesDao extends HibernateDao<Sales, Long> {

	@Autowired
	private OrderDao orderDao;
	@Autowired
	private GoodsDao goodsDao;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Pager<Sales> findPager(Pager<Sales> pager, List<PropertyFilter> filters) {
		pager = pager == null ? new Pager<Sales>() : pager;
		Criterion[] criterions = buildPropertyFilterCriterions(filters);
		Criteria c = this.createCriteria(criterions);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("sn"), "sn");
		projectionList.add(Projections.groupProperty("type"), "TYPE");
		projectionList.add(Projections.sum("quantity"), "quantity");
		projectionList.add(Projections.sum("amount"), "amount");

		c.setProjection(projectionList);
		c.setResultTransformer(new AliasToBeanResultTransformer(Sales.class));
		pager.setTotalCount(countCriteriaResult(c));
		setPageParameter(c, pager);
		pager.setPageItems(c.list());

		if (pager.getTotalCount() > 0) {
			Type type = ObjectUtil.find(filters, "filterName", "EQE_type").getPropertyValue(Type.class);
			List targets = new ArrayList();
			if (Type.goods == type) {
				targets = goodsDao.find(Restrictions.in("sn", ObjectUtil.toFieldArray(pager.getPageItems(), "sn", String.class)));
			} else if (Type.order == type) {
				targets = orderDao.find(Restrictions.in("sn", ObjectUtil.toFieldArray(pager.getPageItems(), "sn", String.class)));
			}
			for(Sales sales : pager.getPageItems()){
				sales.setTarget(ObjectUtil.find(targets, "sn", sales.getSn()));
			}
		}
		return pager;
	}

	@SuppressWarnings("unchecked")
	public List<Sales> getSalesBySn(Criterion... criterions) {
		Criteria criteria = this.createCriteria(criterions);

		ProjectionList projectionList = Projections.projectionList();
		projectionList.add(Projections.groupProperty("type"));
		projectionList.add(Projections.groupProperty("sn"), "sn");
		projectionList.add(Projections.sum("quantity"), "quantity");
		projectionList.add(Projections.sum("amount"), "amount");

		criteria.setProjection(projectionList);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(Sales.class));
		return criteria.list();
	}

	public Integer getSaleCount(Sales.Type type, String sn, TimeUnit timeUnit, String start, String end) {
		Criteria criteria = this.createCriteria(new Criterion[] { Restrictions.eq("type", type), Restrictions.eq("sn", sn), Restrictions.eq("timeUnit", timeUnit), Restrictions.ge("time", start), Restrictions.le("time", end) });
		criteria.setProjection(Projections.sum("quantity"));
		return ((Integer) criteria.uniqueResult());
	}

	public List<Sales> statistics(List<PropertyFilter> filters, Map<String, Projection> projections) {
		Criterion[] criterions = buildPropertyFilterCriterions(filters);
		return this.statistics(criterions, projections, Sales.class);
	}

	public <E> List<E> statistics(List<PropertyFilter> filters, Map<String, Projection> projections, Class<E> entity) {
		Criterion[] criterions = buildPropertyFilterCriterions(filters);
		return this.statistics(criterions, projections, entity);
	}

	public List<Sales> statistics(Criterion[] criterions, Map<String, Projection> projections) {
		return this.statistics(criterions, projections, Sales.class);
	}

	@SuppressWarnings("unchecked")
	public <E> List<E> statistics(Criterion[] criterions, Map<String, Projection> projections, Class<E> entity) {
		Criteria criteria = this.createCriteria(criterions);
		ProjectionList projectionList = Projections.projectionList();
		for (Map.Entry<String, Projection> entry : projections.entrySet()) {
			projectionList.add(entry.getValue(), entry.getKey());
		}
		criteria.setProjection(projectionList);
		criteria.setResultTransformer(new AliasToBeanResultTransformer(entity));
		return criteria.list();
	}

}
