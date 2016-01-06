package org.jfantasy.mall.goods.interceptor;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.mall.goods.bean.Goods;
import org.jfantasy.mall.goods.bean.GoodsCategory;
import org.jfantasy.mall.goods.bean.GoodsParameter;
import org.jfantasy.mall.goods.bean.GoodsParameterValue;
import org.jfantasy.mall.goods.service.GoodsService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Component
//@Aspect
public class GoodsParameterValueInterceptor {
	
	@Autowired
	private GoodsService goodsService; 

	@SuppressWarnings("unchecked")
	@Before("execution(public * org.jfantasy.mall.goods.service.GoodsService.save(org.jfantasy.mall.goods.bean.Goods))")
	public void save(JoinPoint point) {
		Goods goods = (Goods) point.getArgs()[0];
		GoodsCategory category = goods.getId() != null ? goodsService.getGoods(goods.getId()).getCategory() : goodsService.getCategory(goods.getCategory().getId());
		List<GoodsParameterValue> allGoodsParameterValues = new ArrayList<GoodsParameterValue>();
		for(GoodsParameterValue goodsParameterValue : (List<GoodsParameterValue>)ObjectUtil.defaultValue(goods.getGoodsParameterValues(), Collections.EMPTY_LIST)){
			GoodsParameter goodsParameter = ObjectUtil.find(category.getGoodsParameters(), "id", goodsParameterValue.getId());
			if(goodsParameter == null){
				continue;
			}
			goodsParameterValue.setName(goodsParameter.getName());
			allGoodsParameterValues.add(goodsParameterValue);
			goodsParameterValue.setSort(allGoodsParameterValues.size());
		}
		for(GoodsParameterValue goodsParameterValue :(List<GoodsParameterValue>)ObjectUtil.defaultValue(goods.getCustomGoodsParameterValues(), Collections.EMPTY_LIST)){
			allGoodsParameterValues.add(goodsParameterValue);
			goodsParameterValue.setSort(allGoodsParameterValues.size());
		}
//		goods.setGoodsParameterValueStore(JSON.serialize(allGoodsParameterValues));
	}
	
}
