package com.fantasy.mall.goods.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fantasy.framework.dao.hibernate.HibernateDao;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;

@Repository
public class GoodsDao extends HibernateDao<Goods, Long> {

	@Override
	protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
		PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_category.sign");
		if (filter != null) {// 将编号转换为id
			filters.add(new PropertyFilter("EQL_category.id", getGoodsCategoryIdUniqueBySign(filter.getPropertyValue(String.class))));
		}
		filter = ObjectUtil.remove(filters, "filterName", "EQL_category.id");
		if (filter == null) {// 如果没有设置分类条件，默认为根
			String[] ids = getRootGoodsCategoryIds();
			if (ids.length > 1) {
				filter = new PropertyFilter("INL_category.id", ids);
			} else {
				filter = new PropertyFilter("EQL_category.id", ids[0]);
			}
		}
		Criterion likePathCriterion = null;
		// 将 id 转为 path like 查询
		if (filter.getMatchType() == PropertyFilter.MatchType.IN) {
			Disjunction disjunction = Restrictions.disjunction();
			for (Long id : filter.getPropertyValue(Long[].class)) {
				GoodsCategory category = (GoodsCategory) this.getSession().get(GoodsCategory.class, id);
				disjunction.add(Restrictions.like("category.path", category.getPath(), MatchMode.START));
			}
			likePathCriterion = disjunction;
		} else {
			GoodsCategory category = (GoodsCategory) this.getSession().get(GoodsCategory.class, (Serializable) filter.getPropertyValue());
			likePathCriterion = Restrictions.like("category.path", category.getPath(), MatchMode.START);
		}
		filters = filters == null ? new ArrayList<PropertyFilter>() : filters;
		Criterion[] criterions = super.buildPropertyFilterCriterions(filters);
		criterions = ObjectUtil.join(criterions, likePathCriterion);
		return criterions;
	}

	@SuppressWarnings("unchecked")
	private String[] getRootGoodsCategoryIds() {
		String ids = ObjectUtil.toString(this.getSession().createCriteria(GoodsCategory.class).add(Restrictions.isNull("parent")).list(), "id", ";");
		return StringUtil.tokenizeToStringArray(ids);
	}

	private String getGoodsCategoryIdUniqueBySign(String sign) {
		return ((GoodsCategory) this.getSession().createCriteria(GoodsCategory.class).add(Restrictions.eq("sign", sign)).uniqueResult()).getId().toString();
	}

}
