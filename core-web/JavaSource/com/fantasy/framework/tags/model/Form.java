package com.fantasy.framework.tags.model;

public class Form extends UIBean {
    public static final String OPEN_TEMPLATE = "form.ftl";
    public static final String TEMPLATE = "form-close.ftl";
    private String action;
    private String target;
    private String method;

    public String getDefaultOpenTemplate() {
	return "form.ftl";
    }

    public String getDefaultTemplate() {
	return "form-close.ftl";
    }

    public String getAction() {
	return this.action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getTarget() {
	return this.target;
    }

    public void setTarget(String target) {
	this.target = target;
    }

    public String getMethod() {
	return this.method;
    }

    public void setMethod(String method) {
	this.method = method;
    }
}