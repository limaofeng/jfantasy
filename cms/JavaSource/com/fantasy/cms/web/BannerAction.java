package com.fantasy.cms.web;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.service.BannerService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import javax.annotation.Resource;
import java.util.List;

/**
 * 轮播图Action
 * 
 * @author li_huang
 * 
 */
public class BannerAction extends ActionSupport {
	private static final long serialVersionUID = 3799834983783507214L;

	@Resource
	private transient BannerService bannerService;


	/**
	 * 首页
	 * 
	 * @return
	 */
	public String index(Pager<Banner> pager, List<PropertyFilter> filters) {
		this.search(pager, filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
	
		return SUCCESS;
	}

	/**
	 * 搜索
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
	public String search(Pager<Banner> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, bannerService.findPager(pager, filters));
		return JSONDATA;
	}

	/**
	 * 保存
	 * 
	 * @param banner
	 * @return
	 */
	public String save(Banner banner) {
		this.attrs.put(ROOT, bannerService.save(banner));
		return JSONDATA;
	}

	/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
	public String edit(Long id) {
		this.attrs.put("banner", this.bannerService.get(id));
		return SUCCESS;
	}
	
	/**
	 * 删除
	 * 
	 * @param ids
	 * @return
	 */
	
	public String delete(Long... ids) {
		this.bannerService.delete(ids);
		return JSONDATA;
	}
}