package com.fantasy.framework.spring.mvc.http.jsonfilter;


import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class NoneFieldsBeanPropertyFilter extends CustomFieldsBeanPropertyFilter {

    @Override
    protected boolean include(PropertyWriter writer) {
        return isDisplay(writer.getName(), ((BeanPropertyWriter) writer).getMember().getDeclaringClass());
    }

}
