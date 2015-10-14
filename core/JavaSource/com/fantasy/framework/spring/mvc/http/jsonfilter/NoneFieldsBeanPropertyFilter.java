package com.fantasy.framework.spring.mvc.http.jsonfilter;


import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.framework.util.common.ClassUtil;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class NoneFieldsBeanPropertyFilter extends CustomFieldsBeanPropertyFilter {

    @Override
    protected boolean include(PropertyWriter writer) {
        Class entityClass = ClassUtil.getRealClass(((BeanPropertyWriter) writer).getMember().getDeclaringClass());
        return BaseBusEntity.class != entityClass && isDisplay(writer.getName(), entityClass);
    }

}
