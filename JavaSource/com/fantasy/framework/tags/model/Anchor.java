package com.fantasy.framework.tags.model;

public class Anchor extends UIBean {
	public static final String OPEN_TEMPLATE = "a.ftl";
	public static final String TEMPLATE = "a-close.ftl";

	public String getDefaultOpenTemplate() {
		return OPEN_TEMPLATE;
	}

	public String getDefaultTemplate() {
		return TEMPLATE;
	}
}
