package com.fantasy.swp.web;

import java.util.List;

import javax.annotation.Resource;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.swp.bean.Data;
import com.fantasy.swp.service.DataService;

/**
 *@Author lsz
 *@Date 2014-2-18 下午3:35:50
 *
 */
public class DataAction extends ActionSupport {
	
	private static final long serialVersionUID = -5178279947544914476L;
	
	@Autowired
	private DataService dataService;
	
	public String index(List<PropertyFilter> filters){
		this.search(new Pager<Data>(), filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	public String search(Pager<Data> pager,List<PropertyFilter> filters){
		this.attrs.put(ROOT, this.dataService.findPager(pager, filters));
		return JSONDATA;
	}
	
	public String save(Data data){
		this.dataService.save(data);
		return JSONDATA;
	}
	
	public String edit(Long id){
		this.attrs.put("data", this.dataService.getData(id));
		return SUCCESS;
	}
	
	public String delete(Long[] ids){
		this.dataService.delete(ids);
		return JSONDATA;
	}
	

}

