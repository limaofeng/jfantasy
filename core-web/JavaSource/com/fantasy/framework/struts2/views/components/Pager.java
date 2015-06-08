package com.fantasy.framework.struts2.views.components;

import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

@StrutsTag(name = "pager", tldTagClass = "com.fantasy.framework.struts2.views.jsp.ui.PagerTag", description = "")
public class Pager extends UIBean {
	private static final String TEMPLATE = "pager";
	private int totalCount = 0;
	private int pageSize = 0;
	private int totalPage = 1;
	private int currentPage = 1;
	private String url = "###";

	public Pager(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		addParameter("totalCount", Integer.valueOf(this.totalCount));
		addParameter("pageSize", Integer.valueOf(this.pageSize));
		addParameter("totalPage", Integer.valueOf(this.totalPage));
		addParameter("currentPage", Integer.valueOf(this.currentPage));
		addParameter("url", this.url);
	}

	@StrutsTagAttribute(description = "", type = "int")
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	@StrutsTagAttribute(description = "", type = "int")
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	@StrutsTagAttribute(description = "", type = "int")
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	@StrutsTagAttribute(description = "", type = "int")
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return this.totalCount;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public int getTotalPage() {
		return this.totalPage;
	}

	public int getCurrentPage() {
		return this.currentPage;
	}

	public String getUrl() {
		return this.url;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setUrl(String url) {
		this.url = url;
	}
}