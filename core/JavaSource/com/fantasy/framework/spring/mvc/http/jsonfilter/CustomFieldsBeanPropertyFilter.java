package com.fantasy.framework.spring.mvc.http.jsonfilter;

import com.fantasy.framework.util.Stack;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

public abstract class CustomFieldsBeanPropertyFilter extends SimpleBeanPropertyFilter {
    protected Stack<Capsule> stack = new Stack<Capsule>();

    @Override
    protected boolean include(BeanPropertyWriter writer) {
        return false;
    }

    protected static boolean isResultFields(String name, String[] resultFields) {
        return ObjectUtil.indexOf(resultFields, name) != -1;
    }

    protected static boolean isDisplay(String name, Class entityClass) {
        JsonFilter jsonFilter = ClassUtil.getAnnotation(entityClass, JsonFilter.class);
        String[] ignoreProperties = new String[0];
        if (jsonFilter != null && JSON.CUSTOM_FILTER.equals(jsonFilter.value())) {
            ignoreProperties = JSON.getIgnoreProperties(entityClass);
        }
        return ObjectUtil.indexOf(ignoreProperties, name) == -1;
    }

    public static class Capsule {
        private Class<?> clazz;
        private String name;

        public Capsule(Class<?> clazz, String name) {
            this.clazz = clazz;
            this.name = name;
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
