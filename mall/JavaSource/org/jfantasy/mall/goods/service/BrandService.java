package org.jfantasy.mall.goods.service;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.mall.goods.bean.Brand;
import org.jfantasy.mall.goods.dao.BrandDao;
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
	public void delete(Long... ids) {
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
	public Brand get(Long id) {
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

}
