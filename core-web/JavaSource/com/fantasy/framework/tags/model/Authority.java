package com.fantasy.framework.tags.model;

public class Authority extends UIBean {
    private String url;

    public String getDefaultOpenTemplate() {
	return "";
    }

    public String getDefaultTemplate() {
	return "";
    }

    public String getUrl() {
	return this.url;
    }

    public void setUrl(String url) {
	this.url = url;
    }
}