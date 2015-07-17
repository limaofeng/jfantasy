package com.fantasy.framework.tags.model;

import com.fantasy.security.bean.Menu;

import java.util.List;

public class Navigation extends UIBean {
    public static final String OPEN_TEMPLATE = "nav.ftl";
    public static final String TEMPLATE = "nav-close.ftl";
    private List<Menu> menus;

    public String getDefaultOpenTemplate() {
	return "nav.ftl";
    }

    public String getDefaultTemplate() {
	return "nav-close.ftl";
    }

    public List<Menu> getMenus() {
	return this.menus;
    }

    public void setMenus(List<Menu> menus) {
	this.menus = menus;
    }
}