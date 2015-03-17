package com.fantasy.file.web;

import java.util.List;

import javax.annotation.Resource;

import com.fantasy.file.bean.Directory;
import com.fantasy.file.service.DirectoryService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

public class DirectoryAction extends ActionSupport {

	private static final long serialVersionUID = 7523936626517724295L;
	
	@Autowired
	private transient DirectoryService directoryService;
	
	public String index(Pager<Directory> pager, List<PropertyFilter> filters) {
		this.search(pager, filters);
		this.attrs.put("pager", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	public String search(Pager<Directory> pager, List<PropertyFilter> filters) {
		this.attrs.put(ROOT, directoryService.findPager(pager, filters));
		return JSONDATA;
	}
	
	
	public String delete(String... keys) {
		this.directoryService.delete(keys);
		return JSONDATA;
	}
	
	public String save(Directory directory) {
		this.attrs.put(ROOT, directoryService.save(directory));
		return JSONDATA;
	}
	
	public String edit(String id) {
		this.attrs.put("directory", this.directoryService.get(id));
	
		return SUCCESS;
	}
	
}
