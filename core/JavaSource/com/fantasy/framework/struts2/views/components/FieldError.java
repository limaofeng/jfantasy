package com.fantasy.framework.struts2.views.components;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.UIBean;
import org.apache.struts2.components.Param.UnnamedParametric;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

import com.opensymphony.xwork2.util.ValueStack;

@StrutsTag(name = "fielderror", tldTagClass = "org.apache.struts2.views.jsp.ui.FieldErrorTag", description = "Render field error (all or partial depending on param tag nested)if they exists")
public class FieldError extends UIBean implements UnnamedParametric {
    private List<String> errorFieldNames = new ArrayList<String>();
    private boolean escape = true;
    private String tagName = "span";
    private static final String TEMPLATE = "fielderror";

    public FieldError(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
	super(stack, request, response);
    }

    protected String getDefaultTemplate() {
	return TEMPLATE;
    }

    protected void evaluateExtraParams() {
	super.evaluateExtraParams();

	if (this.errorFieldNames != null) {
	    addParameter("errorFieldNames", this.errorFieldNames);
	}
	addParameter("escape", Boolean.valueOf(this.escape));
	addParameter("tagName", this.tagName);
    }

    public void addParameter(Object value) {
	if (value != null)
	    this.errorFieldNames.add(value.toString());
    }

    public List<String> getFieldErrorFieldNames() {
	return this.errorFieldNames;
    }

    @StrutsTagAttribute(description = "Field name for single field attribute usage", type = "String")
    public void setFieldName(String fieldName) {
	addParameter(fieldName);
    }

    @StrutsTagAttribute(description = " Whether to escape HTML", type = "Boolean", defaultValue = "true")
    public void setEscape(boolean escape) {
	this.escape = escape;
    }

    public String getTagName() {
	return this.tagName;
    }

    @StrutsTagAttribute(description = "", type = "String", defaultValue = "span")
    public void setTagName(String tagName) {
	this.tagName = tagName;
    }
}