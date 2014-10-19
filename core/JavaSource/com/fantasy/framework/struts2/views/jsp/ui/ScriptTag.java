package com.fantasy.framework.struts2.views.jsp.ui;

import com.fantasy.framework.struts2.views.components.DynamicScript;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.util.ValueStack;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.Component;
import org.apache.struts2.views.jsp.ui.AbstractUITag;

public class ScriptTag extends AbstractUITag {
    private static final long serialVersionUID = 4346490257536592107L;
    private String type;
    private String action;
    private String namespace;
    private String charset;
    private String formId;
    private String iErrorClass;
    private String errorClass;
    private String correctClass;

    public Component getBean(ValueStack stack, HttpServletRequest req, HttpServletResponse res) {
	return new DynamicScript(stack, req, res);
    }

    protected void populateParams() {
	super.populateParams();
	DynamicScript dynamicScript = (DynamicScript) this.component;
	if (StringUtil.isNotBlank(this.type))
	    dynamicScript.setType(this.type);
	if (StringUtil.isNotBlank(this.charset))
	    dynamicScript.setCharset(this.charset);
	if (StringUtil.isNotBlank(this.namespace))
	    dynamicScript.setNamespace(this.namespace);
	if (StringUtil.isNotBlank(this.formId))
	    dynamicScript.setFormId(this.formId);
	if (StringUtil.isNotBlank(this.iErrorClass))
	    dynamicScript.setiErrorClass(this.iErrorClass);
	if (StringUtil.isNotBlank(this.errorClass))
	    dynamicScript.setErrorClass(this.errorClass);
	if (StringUtil.isNotBlank(this.correctClass)) {
	    dynamicScript.setCorrectClass(this.correctClass);
	}

	dynamicScript.setAction(this.action);
    }

    public void setType(String type) {
	this.type = type;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public void setNamespace(String namespace) {
	this.namespace = namespace;
    }

    public void setCharset(String charset) {
	this.charset = charset;
    }

    public void setFormId(String formId) {
	this.formId = formId;
    }

    public void setiErrorClass(String iErrorClass) {
	this.iErrorClass = iErrorClass;
    }

    public void setErrorClass(String errorClass) {
	this.errorClass = errorClass;
    }

    public void setCorrectClass(String correctClass) {
	this.correctClass = correctClass;
    }
}