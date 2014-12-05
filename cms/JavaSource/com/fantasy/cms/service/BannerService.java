package com.fantasy.cms.service;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.bean.BannerItem;
import com.fantasy.cms.dao.BannerDao;
import com.fantasy.cms.dao.BannerItemDao;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.ObjectUtil;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * 轮播图Service
 * 
 * @author li_huang
 * 
 */
@Service("fantasy.cms.BannerService")
@Transactional
public class BannerService {

	@Resource
	private BannerDao bannerDao;

	@Resource
	private BannerItemDao bannerItemDao;

	public Pager<Banner> findPager(Pager<Banner> pager, List<PropertyFilter> filters) {
		return bannerDao.findPager(pager, filters);
	}

	@SuppressWarnings("unchecked")
	public Banner save(Banner banner) {
		List<BannerItem> bitems = banner.getBannerItems();// 传递过来的集合
		bannerDao.save(banner);
		List<BannerItem> banntiems = banner.getBannerItems();// 数据库中原来就有的集合
		if (bitems == null) {
			bitems = Collections.emptyList();
		}
		if (banntiems == null) {
			banntiems = Collections.emptyList();
		}
		for (BannerItem bnitem : bitems) {
			bnitem.setBanner(banner);
			bannerItemDao.save(bnitem);
			if (!banntiems.equals(bitems)) {// 添加时，这两个集合为同一个，所以不能做移除操作
				ObjectUtil.remove(banntiems, "id", bnitem.getId());
			}
		}
		if (!banntiems.equals(bitems)) {// 添加时，这两个集合为同一个，所以不能做移除操作
			for (BannerItem bnitem : banntiems) {
				bannerItemDao.delete(bnitem);
			}
		}
		return banner;

	}

	public Banner get(Long id) {
		Banner banner = bannerDao.get(id);
		Hibernate.initialize(banner);
		for (BannerItem item : banner.getBannerItems()) {
			Hibernate.initialize(item);
		}
		return banner;
	}

	public Banner findUniqueByKey(String key) {
		return this.bannerDao.findUniqueBy("key", key);
	}

	@CacheEvict(value = { "fantasy.cms.bannerService" }, allEntries = true)
	public void delete(Long... ids) {
		for (Long id : ids) {
			this.bannerDao.delete(id);
		}
	}

	public boolean bannerKeyUnique(String key, Long id) {
		Banner banner = this.bannerDao.findUniqueBy("key", key);
		return (banner == null) || banner.getId().equals(id);
	}

}
