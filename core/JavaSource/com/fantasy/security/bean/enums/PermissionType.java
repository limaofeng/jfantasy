package com.fantasy.security.bean.enums;

/**
 * 权限类型
 */
public enum PermissionType {

    any("允许直接访问"), antPath("通配符"), ipAddress("Ip地址"), regex("正则表达式"), and("多组条件同时满足"), or("多条件满足一个");

    private String value;

    PermissionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    }
