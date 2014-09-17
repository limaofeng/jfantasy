package com.fantasy.framework.tags.model;

public class Button extends UIBean {
    public static final String OPEN_TEMPLATE = "button.ftl";
    public static final String TEMPLATE = "button-close.ftl";
    private String url;

    public String getDefaultOpenTemplate() {
	return OPEN_TEMPLATE;
    }

    public String getDefaultTemplate() {
	return TEMPLATE;
    }

    public String getUrl() {
	return this.url;
    }

    public void setUrl(String url) {
	this.url = url;
    }
}
