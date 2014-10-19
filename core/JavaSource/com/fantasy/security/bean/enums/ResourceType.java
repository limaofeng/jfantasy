package com.fantasy.security.bean.enums;

public enum ResourceType {

    url("url"), any("不需要拦截"), antPath("通配符"), ipAddress("Ip地址"), regex("正则表达式"), group("组");

    private String value;

    private ResourceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}