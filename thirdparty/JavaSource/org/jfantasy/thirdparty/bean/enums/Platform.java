package org.jfantasy.thirdparty.bean.enums;

/**
 * 开放平台简码
 */
public enum Platform {

    weixin("微信");

    private String value;

    Platform(String name) {
        this.value = name;
    }


    public String getValue() {
        return this.value;
    }

}
