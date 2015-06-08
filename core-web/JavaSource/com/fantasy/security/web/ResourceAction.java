package com.fantasy.security.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.security.bean.Resource;
import com.fantasy.security.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ResourceAction extends ActionSupport {

	private static final long serialVersionUID = 3747889586663994886L;

	@Autowired
	private transient ResourceService resourceService;
	
	public String index(){
		this.attrs.put("resources",this.resourceService.getTreeResource());
		this.search(new Pager<Resource>(),new ArrayList<PropertyFilter>());
		this.attrs.put("pager", this.attrs.get(ROOT));
		return SUCCESS;
	}
	public String search(Pager<Resource> pager,List<PropertyFilter> filters){
		filters.add(new PropertyFilter("EQE_type","url"));
		this.attrs.put(ROOT,this.resourceService.search(pager, filters));
		return  JSONDATA;
	}
	public String save(Resource resource) {
		this.resourceService.save(resource);
		this.attrs.put(ROOT, resource);
		return JSONDATA;
	}
	
	public String edit(Long id){
		this.attrs.put("resource",this.resourceService.get(id));
		return SUCCESS;
	}

	public String delete(Long[] ids){
		this.resourceService.delete(ids);
		return JSONDATA;
	}



}
