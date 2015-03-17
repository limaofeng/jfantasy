package com.fantasy.file.web;

import com.fantasy.file.bean.FileManagerConfig;
import com.fantasy.file.service.FileManagerService;
import com.fantasy.framework.struts2.ActionSupport;

import org.springframework.beans.factory.annotation.Autowired;

public class FileManagerAction extends ActionSupport {

	private static final long serialVersionUID = 4509625889822157981L;

	@Autowired
	private FileManagerService fileManagerService;

	/**
	 * 文件管理器
	 * 
	 * @功能描述
	 * @return
	 */
	public String index() {
		this.search();
		this.attrs.put("list", this.attrs.get(ROOT));
		this.attrs.remove(ROOT);
		return SUCCESS;
	}
	
	public String search(){
		this.attrs.put(ROOT, fileManagerService.getAll());
		return JSONDATA;
	}
	
	/**
	 * 编辑文件管理器配置
	 * 
	 * @功能描述
	 * @param id
	 * @return
	 */
	public String edit(String id) {
		this.attrs.put("fm", fileManagerService.get(id));
		return SUCCESS;
	}

	/**
	 * 保存文件管理器
	 * 
	 * @功能描述
	 * @param fm
	 * @return
	 */
	public String save(FileManagerConfig fm) {
		this.attrs.put(ROOT, fileManagerService.save(fm));
		return JSONDATA;
	}
	
	/**
	 * 查看
	 * @param id
	 * @return
	 */
	public String view(String id){
		this.attrs.put("fm", this.fileManagerService.get(id));
		return SUCCESS;
	}

	/**
	 * 删除文件管理器
	 * 
	 * @功能描述
	 * @param ids
	 * @return
	 */
	public String delete(String[] ids) {
		this.fileManagerService.delete(ids);
		return JSONDATA;
	}

}
