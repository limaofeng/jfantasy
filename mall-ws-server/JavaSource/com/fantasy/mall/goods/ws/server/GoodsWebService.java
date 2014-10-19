package com.fantasy.mall.goods.ws.server;

import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.mall.goods.bean.Brand;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;
import com.fantasy.mall.goods.service.BrandService;
import com.fantasy.mall.goods.service.GoodsService;
import com.fantasy.mall.goods.ws.IGoodsService;
import com.fantasy.mall.goods.ws.dto.*;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class GoodsWebService implements IGoodsService {

	@Resource
	private GoodsService goodsService;
	@Resource
	private BrandService brandService;
	
	@Override
	public PagerDTO findPager(PagerDTO pager, PropertyFilterDTO[] filters) {
		return WebServiceUtil.toPager(pager, goodsService.findPager(WebServiceUtil.toPager(pager, Goods.class), WebServiceUtil.toFilters(filters)));
	}
	
	@Override
	public GoodsDTO getGoodsById(Long id) {
		Goods goods = goodsService.getGoods(id);
		GoodsDTO dto = WebServiceUtil.toBean(goods,GoodsDTO.class);
		dto.setGoodsParameterValues(WebServiceUtil.toArray(goods.getGoodsParameterValues(), GoodsParameterValueDTO.class));
		dto.setGoodsImages(WebServiceUtil.toArray(goods.getGoodsImages(), GoodsImageDTO.class));
		dto.setIntroduction(goods.getIntroduction());
		return dto;
	}

	@Override
	public BrandDTO[] brands() {
		List<Brand> brands = brandService.getBrands();
		return WebServiceUtil.toArray(brands, BrandDTO.class);
	}
	
	@Override
	public GoodsCategoryDTO[] categories() {
		List<GoodsCategory> categories = goodsService.getCategories();
		return WebServiceUtil.toArray(categories, GoodsCategoryDTO.class);
	}

	@Override
	public GoodsDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
		return WebServiceUtil.toArray(goodsService.find(WebServiceUtil.toFilters(filters), orderBy, order, 0, size), GoodsDTO.class);
	}

	@Override
	public GoodsCategoryDTO getGoodsCategoryBySign(String sign) {
		GoodsCategory category = goodsService.getCategoryBySign(sign);
		Hibernate.initialize(category.getChildren());
		GoodsCategoryDTO dto = WebServiceUtil.toBean(category, GoodsCategoryDTO.class);
		dto.setChildren(WebServiceUtil.toArray(category.getChildren(),GoodsCategoryDTO.class));
		return dto;
	}
}
