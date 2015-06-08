package com.fantasy.file.web;

import com.fantasy.common.bean.FtpConfig;
import com.fantasy.common.service.FtpConfigService;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.struts2.ActionSupport;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

public class FtpConfigAction extends ActionSupport {

	private static final long serialVersionUID = 1L;

	@Autowired
	private FtpConfigService ftpConfigService;

    public String queryList(List<PropertyFilter> filters){
        this.search(filters);
        this.attrs.put("list", this.attrs.get(ROOT));
        this.attrs.remove(ROOT);
        return SUCCESS;
    }

	public String search(List<PropertyFilter> filters) {
		this.attrs.put(ROOT, ftpConfigService.find(filters));
		return JSONDATA;
	}

	public String edit(Long id) {
		this.attrs.put("ftp", ftpConfigService.get(id));
		return SUCCESS;
	}

	public String save(FtpConfig config) {
		this.ftpConfigService.save(config);
		return JSONDATA;
	}

	public String delete(Long... ids) {
		this.ftpConfigService.delete(ids);
		return JSONDATA;
	}

}
