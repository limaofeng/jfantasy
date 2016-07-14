package org.jfantasy.security.bean.enums;

public enum MenuType {
    menu("菜单"), operation("操作");

    private String value;

    MenuType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}