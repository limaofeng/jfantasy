package com.fantasy.website.web;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;
import com.fantasy.website.bean.DataInferface;
import com.fantasy.website.service.DataInferfaceService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 *@Author lsz
 *@Date 2014-2-18 上午10:34:52
 *
 */
public class DataInferfaceAction extends ActionSupport {

	private static final long serialVersionUID = -8044736814850725573L;
	
	@Autowired
	private DataInferfaceService faceService;
	
	public String index(List<PropertyFilter> filters){
		this.search(new Pager<DataInferface>(),filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	public String search(Pager<DataInferface> pager,List<PropertyFilter> filters){
		this.attrs.put(ROOT, this.faceService.findPager(pager, filters));
		return JSONDATA;
	}
	
	public String save(DataInferface face){
		this.faceService.save(face);
		return JSONDATA;
	}
	
	public String edit(Long id){
		this.attrs.put("face",this.faceService.getDataInferface(id));
		return SUCCESS;
	}
	
	public String delete(Long[] ids){
		this.faceService.delete(ids);
		return JSONDATA;
	}

}

