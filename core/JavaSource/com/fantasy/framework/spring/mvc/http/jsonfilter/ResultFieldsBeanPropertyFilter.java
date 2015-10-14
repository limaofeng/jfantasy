package com.fantasy.framework.spring.mvc.http.jsonfilter;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ClassUtil;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class ResultFieldsBeanPropertyFilter extends CustomFieldsBeanPropertyFilter {
    private String[] resultFields;

    public ResultFieldsBeanPropertyFilter(String... resultFields) {
        this.resultFields = resultFields;
    }

    @Override
    protected boolean include(PropertyWriter writer) {
        String name = writer.getName();
        Class entityClass = ((BeanPropertyWriter) writer).getMember().getDeclaringClass();
        Class propertyType = ((BeanPropertyWriter) writer).getPropertyType();

        if (ClassUtil.getDeclaredField(BaseBusEntity.class, name) != null) {
            return isResultFields(name, resultFields);
        }

        boolean display = isResultFields(name, resultFields);

        Capsule capsule = this.clearCapsule(entityClass, propertyType);
        if (!display) {
            String prefix = capsule != null ? capsule.getPrefix() : "";
            display = isResultFields(prefix + name, resultFields);
        }

        try {
            return display;
        } finally {
            if (display) {
                addCapsule(name, entityClass, propertyType);
            }
        }
    }


}