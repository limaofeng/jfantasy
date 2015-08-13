package com.fantasy.attr.framework.util;


import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.file.bean.FileDetail;

import java.util.Date;

public class AttributeUtils {

    private AttributeUtils() {
    }

    public static Attribute string(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(String.class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute strings(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(String[].class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute bean(String code, String name, String description, Class<?> javaType) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(javaType));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute file(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(FileDetail.class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute files(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(FileDetail[].class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute integer(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(Integer.class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute integers(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(Integer[].class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute date(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(Date.class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

    public static Attribute dates(String code, String name, String description) {
        Attribute attribute = new Attribute();
        attribute.setCode(code);
        attribute.setName(name);
        attribute.setDescription(description);
        attribute.setAttributeType(AttributeTypeUtils.get(Date[].class));
        attribute.setNonNull(true);
        attribute.setNotTemporary(false);
        return attribute;
    }

}
