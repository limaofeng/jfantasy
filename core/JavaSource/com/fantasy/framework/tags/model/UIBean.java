package com.fantasy.framework.tags.model;

public abstract class UIBean {
    private String id;
    private String name;
    private String href;
    private String onclick;
    private String cssClass;
    private String style;
    private String title;

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return this.name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getHref() {
	return this.href;
    }

    public void setHref(String href) {
	this.href = href;
    }

    public String getOnclick() {
	return this.onclick;
    }

    public void setOnclick(String onclick) {
	this.onclick = onclick;
    }

    public String getCssClass() {
	return this.cssClass;
    }

    public void setCssClass(String cssClass) {
	this.cssClass = cssClass;
    }

    public String getStyle() {
	return this.style;
    }

    public void setStyle(String style) {
	this.style = style;
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public abstract String getDefaultTemplate();

    public abstract String getDefaultOpenTemplate();
}
