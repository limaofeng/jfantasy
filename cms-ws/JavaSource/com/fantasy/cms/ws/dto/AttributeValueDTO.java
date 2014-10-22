package com.fantasy.cms.ws.dto;

/**
 * Created by hebo on 2014/10/22.
 */
public class AttributeValueDTO {

    /**
     * 字段名称
     */
    private String name;


    /**
     * 字段值
     */
    private String value;


    /**
     * 字段类型
     */
    private String type;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
