package com.fantasy.framework.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.util.ValueStack;

public class PagerTag extends AbstractUITag {
	private static final long serialVersionUID = 6120733950979340310L;
	private int totalCount;
	private int pageSize;
	private int totalPage;
	private int currentPage;
	private String url;
	private String value;

	public Component getBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		return new com.fantasy.framework.struts2.views.components.Pager(stack, request, response);
	}

	protected void populateParams() {
		super.populateParams();
		com.fantasy.framework.struts2.views.components.Pager pager = (com.fantasy.framework.struts2.views.components.Pager) this.component;
		if (StringUtil.isNotBlank(this.value)) {
			com.fantasy.framework.dao.Pager<?> page = (com.fantasy.framework.dao.Pager<?>) getStack().findValue(this.value);
			if (ObjectUtil.isNotNull(page)) {
				pager.setCurrentPage(page.getCurrentPage());
				pager.setTotalCount(page.getTotalCount());
				pager.setTotalPage(page.getTotalPage());
				pager.setPageSize(page.getPageSize());
			}
		} else {
			pager.setCurrentPage(this.currentPage);
			pager.setTotalCount(this.totalCount);
			pager.setTotalPage(this.totalPage);
			pager.setPageSize(this.pageSize);
		}
		pager.setUrl(this.url);
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setValue(String value) {
		this.value = value;
	}

}