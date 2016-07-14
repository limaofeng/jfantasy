package org.jfantasy.common.bean.enums;

public enum AreaTag {
    country("国家"), state("省/洲"), city("城市"),region("区"), street("街道");

    private String value;

    AreaTag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
