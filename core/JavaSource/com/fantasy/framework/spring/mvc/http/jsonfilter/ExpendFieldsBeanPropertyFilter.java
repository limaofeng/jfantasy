package com.fantasy.framework.spring.mvc.http.jsonfilter;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ClassUtil;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class ExpendFieldsBeanPropertyFilter extends CustomFieldsBeanPropertyFilter {

    private String[] expendFields;

    public ExpendFieldsBeanPropertyFilter(String... expendFields) {
        this.expendFields = expendFields;
    }

    @Override
    protected boolean include(PropertyWriter writer) {
        String name = writer.getName();
        Class entityClass = ClassUtil.getRealClass(((BeanPropertyWriter) writer).getMember().getDeclaringClass());
        Class propertyType = ((BeanPropertyWriter) writer).getPropertyType();

        if (ClassUtil.getDeclaredField(BaseBusEntity.class, name) != null) {
            return isResultFields(name, expendFields);
        }

        Capsule capsule = this.clearCapsule(entityClass, propertyType);

        boolean display = isDisplay(name, entityClass);
        if (!display) {
            String prefix = capsule != null ? capsule.getPrefix() : "";
            display = isResultFields(prefix + name, expendFields);
        }

        try {
            return display;
        } finally {
            if (display) {
                this.addCapsule(name, entityClass, propertyType);
            }
        }
    }
}