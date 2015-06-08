package com.fantasy.common.web;

import com.fantasy.framework.dao.mybatis.keygen.GUIDKeyGenerator;
import com.fantasy.framework.struts2.ActionSupport;

public class CommonAction extends ActionSupport {

	private static final long serialVersionUID = -1600105322972157708L;

	/**
	 * 获取guid
	 * 
	 * @功能描述
	 * @return
	 */
	public String guid() {
		this.attrs.put("guid", GUIDKeyGenerator.getInstance().getGUID());
		return JSONDATA;
	}
	
}
