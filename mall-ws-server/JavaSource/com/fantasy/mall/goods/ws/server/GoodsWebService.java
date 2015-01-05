package com.fantasy.mall.goods.ws.server;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.ws.util.PagerDTO;
import com.fantasy.framework.ws.util.PropertyFilterDTO;
import com.fantasy.framework.ws.util.WebServiceUtil;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;
import com.fantasy.mall.goods.service.BrandService;
import com.fantasy.mall.goods.service.GoodsService;
import com.fantasy.mall.goods.ws.IGoodsService;
import com.fantasy.mall.goods.ws.dto.GoodsCategoryDTO;
import com.fantasy.mall.goods.ws.dto.GoodsDTO;
import com.fantasy.mall.goods.ws.dto.GoodsPagerResult;
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
	public GoodsPagerResult findPager(PagerDTO pager, PropertyFilterDTO[] filters) {
		Pager<Goods> _pager = goodsService.findPager(WebServiceUtil.toPager(pager, Goods.class), WebServiceUtil.toFilters(filters));
		return WebServiceUtil.toPagerResult(_pager,new GoodsPagerResult(),asArray(_pager.getPageItems().toArray(new Goods[_pager.getPageItems().size()])));
	}
	
	@Override
	public GoodsDTO getGoodsById(Long id) {
		return asDto(goodsService.getGoods(id));
	}

	@Override
	public GoodsCategoryDTO[] categories() {
		List<GoodsCategory> goodsCategories = goodsService.getCategories();
		return asArray(goodsCategories.toArray(new GoodsCategory[goodsCategories.size()]));
	}

	@Override
	public GoodsDTO[] find(PropertyFilterDTO[] filters, String orderBy, String order, int size) {
		List<Goods> goodses = goodsService.find(WebServiceUtil.toFilters(filters), orderBy, order, 0, size);
		return asArray(goodses.toArray(new Goods[goodses.size()]));
	}

	@Override
	public GoodsCategoryDTO getGoodsCategoryBySign(String sign) {
		return asDto(goodsService.getCategoryBySign(sign));
	}

	public static GoodsDTO asDto(Goods goods){
//		dto.setGoodsParameterValues(WebServiceUtil.toArray(goods.getGoodsParameterValues(), GoodsParameterValueDTO.class));
//		dto.setGoodsImages(WebServiceUtil.toArray(goods.getGoodsImages(), GoodsImageDTO.class));
		return BeanUtil.copyProperties(new GoodsDTO(),goods);
	}

	public static GoodsDTO[] asArray(Goods[] goodses){
		GoodsDTO[] goodsDTOs = new GoodsDTO[goodses.length];
		for(int i=0;i<goodsDTOs.length;i++){
			goodsDTOs[i] = asDto(goodses[i]);
		}
		return goodsDTOs;
	}

	public static GoodsCategoryDTO asDto(GoodsCategory category){
		return BeanUtil.copyProperties(new GoodsCategoryDTO(),category);
	}

	public static GoodsCategoryDTO[] asArray(GoodsCategory[] categorys){
		GoodsCategoryDTO[] categoryDTOs = new GoodsCategoryDTO[categorys.length];
		for(int i=0;i<categoryDTOs.length;i++){
			categoryDTOs[i] = asDto(categorys[i]);
		}
		return categoryDTOs;
	}

}
