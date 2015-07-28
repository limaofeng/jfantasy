package com.fantasy.security.bean.enums;

public enum UserType {
    member("注册会员"), admin("后台管理员");

    private String value;

    private UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
