package com.fantasy.mall.goods.interceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Component;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsImage;

@Component
@Aspect
public class GoodsImageInterceptor {

	@SuppressWarnings("unchecked")
	@Before("execution(public * com.fantasy.mall.goods.service.GoodsService.save(com.fantasy.mall.goods.bean.Goods))")
	public void save(JoinPoint point) {
		Goods goods = (Goods) point.getArgs()[0];
		List<GoodsImage> goodsImages = StringUtils.isNotBlank(goods.getGoodsImageStore()) ? JSON.deserialize(goods.getGoodsImageStore(), new TypeReference<List<GoodsImage>>() {
		}) : new ArrayList<GoodsImage>();
		for (GoodsImage goodsImage : (List<GoodsImage>) ObjectUtil.defaultValue(goods.getGoodsImages(), Collections.EMPTY_LIST)) {
			GoodsImage source = ObjectUtil.find(goodsImages, "absolutePath", goodsImage.getAbsolutePath());
			if (source != null) {
				source.setFileName(goodsImage.getFileName());
				source.setDescription(goodsImage.getDescription());
			}
		}
		goods.setGoodsImageStore(JSON.serialize(goodsImages));

	}

}
