package com.fantasy.mall.goods.interceptor;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.goods.bean.GoodsCategory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;

import java.util.Collections;

//@Component
//@Aspect
public class GoodsParameterInterceptor {

	@Before("execution(public * com.fantasy.mall.goods.service.GoodsService.save(com.fantasy.mall.goods.bean.GoodsCategory))")
	public void save(JoinPoint point) {
		GoodsCategory goodsCategory = (GoodsCategory) point.getArgs()[0];
		if (goodsCategory.getSort() != null) {
			return;
		}
		goodsCategory.setGoodsParameterStore(JSON.serialize(ObjectUtil.defaultValue(goodsCategory.getGoodsParameters(), Collections.EMPTY_LIST)));
	}

}
