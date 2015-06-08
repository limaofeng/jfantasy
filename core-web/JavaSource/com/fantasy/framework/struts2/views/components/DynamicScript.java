package com.fantasy.framework.struts2.views.components;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "script", tldTagClass = "com.fantasy.framework.struts2.views.jsp.ui.ScriptTag", description = "")
public class DynamicScript extends UIBean {
	private static final String TEMPLATE = "dynamic-script";
	private String action;
	private String namespace;
	private String type = "link";
	private String charset = "utf-8";
	private String formId;
	private String iErrorClass;
	private String errorClass;
	private String correctClass;

	public DynamicScript(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();
		addParameter("action", this.action);
		addParameter("namespace", this.namespace);
		addParameter("type", this.type);
		addParameter("charset", this.charset);
		addParameter("formId", this.formId);
		addParameter("iErrorClass", this.iErrorClass);
		addParameter("errorClass", this.errorClass);
		addParameter("correctClass", this.correctClass);
		addParameter("contextPath", this.request.getContextPath());
	}

	protected String getDefaultTemplate() {
		return TEMPLATE;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setAction(String action) {
		this.action = action;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setType(String type) {
		this.type = type;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setCharset(String charset) {
		this.charset = charset;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setFormId(String formId) {
		this.formId = formId;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setiErrorClass(String iErrorClass) {
		this.iErrorClass = iErrorClass;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setErrorClass(String errorClass) {
		this.errorClass = errorClass;
	}

	@StrutsTagAttribute(description = "", type = "String")
	public void setCorrectClass(String correctClass) {
		this.correctClass = correctClass;
	}
}