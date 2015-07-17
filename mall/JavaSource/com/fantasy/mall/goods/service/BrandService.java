package com.fantasy.mall.goods.service;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.mall.goods.bean.Brand;
import com.fantasy.mall.goods.dao.BrandDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 品牌 service
 * 
 */
@Service
@Transactional
public class BrandService {

	@Autowired
	private BrandDao brandDao;

	/**
	 * 品牌列表
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public Pager<Brand> findPager(Pager<Brand> pager, List<PropertyFilter> filters) {
		return this.brandDao.findPager(pager, filters);
	}

	/**
	 * 批量删除
	 * 
	 * @param ids
	 */
	public void deleteBrand(Long[] ids) {
		for (Long id : ids) {
			this.brandDao.delete(id);
		}
	}

	/**
	 * 获取品牌
	 * 
	 * @param id
	 * @return
	 */
	public Brand getBrand(Long id) {
		return brandDao.get(id);
	}

	/**
	 * 获取品牌
	 * 
	 * @param brand
	 * @return
	 */
	public Brand save(Brand brand) {
		return this.brandDao.save(brand);
	}

	/**
	 * 获取品牌列表
	 * 
	 * @return
	 */
	public List<Brand> getBrands() {
		return this.brandDao.find();
	}

	/**
	 * 静态获取品牌列表
	 * 
	 * @return
	 */
	public static List<Brand> listBrands() {
		BrandService brandService = SpringContextUtil.getBeanByType(BrandService.class);
		return brandService.getBrands();
	}

	/**
	 * 获取品牌
	 * 
	 * @param brandId
	 * @return
	 */
	public static Brand brand(Long brandId) {
		BrandService brandService = SpringContextUtil.getBeanByType(BrandService.class);
		return brandService.getBrand(brandId);
	}

}
