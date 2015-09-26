package com.fantasy.file.bean;

import java.io.Serializable;

public class ConfigParam implements Serializable {

    private String name;
    private String value;

    public ConfigParam() {
    }

    public ConfigParam(String name, String value) {
        this.name = name;
        this.value = value;
    }

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

    @Override
    public String toString() {
        return "ConfigParam{" +
                "name='" + name + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
