package org.jfantasy.mall.goods.dao;

import org.jfantasy.framework.lucene.dao.hibernate.HibernateLuceneDao;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.mall.goods.bean.Goods;
import org.jfantasy.mall.goods.bean.GoodsCategory;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class GoodsDao extends HibernateLuceneDao<Goods, Long> {

	/*
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
		Criterion likePathCriterion;
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
	}*/

	@SuppressWarnings("unchecked")
	private String[] getRootGoodsCategoryIds() {
		String ids = ObjectUtil.toString(this.getSession().createCriteria(GoodsCategory.class).add(Restrictions.isNull("parent")).list(), "id", ";");
		return StringUtil.tokenizeToStringArray(ids);
	}

	private String getGoodsCategoryIdUniqueBySign(String sign) {
		return ((GoodsCategory) this.getSession().createCriteria(GoodsCategory.class).add(Restrictions.eq("sign", sign)).uniqueResult()).getId().toString();
	}
}
