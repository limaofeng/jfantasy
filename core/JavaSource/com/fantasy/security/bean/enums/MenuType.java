package com.fantasy.security.bean.enums;

public enum MenuType {
	url("链接"), menu("菜单"), javascript("脚本"), other("其他");

	private String value;

	private MenuType(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}
}