package com.fantasy.swp.web;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.swp.bean.Page;
import com.fantasy.swp.service.PageService;

/**
 *@Author lsz
 *@Date 2014-1-2 上午11:38:41
 *
 */
public class PageAction extends ActionSupport {
	
	private static final long serialVersionUID = 4701572340953006744L;
	
	@Resource
	private PageService pageService;

	public String index(){
		this.search(new Pager<Page>(),new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	public String search(Pager<Page> pager,List<PropertyFilter> filters){
		this.attrs.put(ROOT,this.pageService.findPager(pager, filters));
		return JSONDATA;
	}
	
	public String save(Page page){
		this.pageService.save(page);
		return JSONDATA;
	}
	
	public String edit(Long id){
		this.attrs.put("page",this.pageService.get(id));
		return SUCCESS;
	}
	
	public String delete(Long[] ids){
		this.pageService.delete(ids);
		return JSONDATA;
	}
	
}

