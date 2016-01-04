package org.jfantasy.mall.goods.dao;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.mall.goods.bean.Product;
import org.hibernate.criterion.Criterion;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("goods.productDao")
public class ProductDao extends HibernateDao<Product, Long> {

	public Pager<Product> findPager(Pager<Product> pager, List<PropertyFilter> filters) {
		// 添加可用库存的查询转换方式
		List<Criterion> usableStoreCriterions = new ArrayList<Criterion>();
		for (PropertyFilter filter : ObjectUtil.filter(filters,"propertyName","usableStore")) {
			usableStoreCriterions.add(sqlRestriction(buildPropertyFilterCriterion(filter.getPropertyName(), filter.getPropertyValue(), filter.getMatchType()), "({alias}.STORE - {alias}.FREEZE_STORE)"));
			filters.remove(filter);
		}
		pager = pager == null ? new Pager<Product>() : pager;
		filters = filters == null ? new ArrayList<PropertyFilter>() : filters;
		Criterion[] criterions = buildPropertyFilterCriterions(filters);
		criterions = ObjectUtil.join(criterions, usableStoreCriterions.toArray(new Criterion[usableStoreCriterions.size()]));
		return findPager(pager, criterions);
	}

}
