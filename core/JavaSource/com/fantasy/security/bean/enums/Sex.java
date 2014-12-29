package com.fantasy.security.bean.enums;

public enum Sex {
    male("男"), female("女"), unknown("未知");

    private String value;

    private Sex(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}