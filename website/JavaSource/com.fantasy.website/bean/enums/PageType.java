package com.fantasy.website.bean.enums;

/**
 * 页面类型
 *
 */
public enum PageType {
    /**
     * 单页面
     */
    single("单页"),
    /**
     * 多页面
     */
    multi("多页"),
    /**
     * 分页
     */
    pagination("分页");

    private String value;

    private PageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
