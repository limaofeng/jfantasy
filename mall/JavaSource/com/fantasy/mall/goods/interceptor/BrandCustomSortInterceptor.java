package com.fantasy.mall.goods.interceptor;

import java.util.Collections;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.mall.goods.bean.GoodsCategory;

@Component
@Aspect
public class BrandCustomSortInterceptor {

	@SuppressWarnings("unchecked")
	@Before("execution(public * com.fantasy.mall.goods.service.GoodsService.save(com.fantasy.mall.goods.bean.GoodsCategory))")
	public void save(JoinPoint point) {
		GoodsCategory goodsCategory = (GoodsCategory) point.getArgs()[0];
		if (goodsCategory.getSort() != null) {
			return;
		}
		goodsCategory.setBrandCustomSort(ObjectUtil.toString(ObjectUtil.defaultValue(goodsCategory.getBrands(), Collections.EMPTY_LIST), "id", ";"));
	}
	
}
