package org.jfantasy.framework.spring.mvc.http.jsonfilter;


import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ClassUtil;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.PropertyWriter;

public class NoneFieldsBeanPropertyFilter extends CustomFieldsBeanPropertyFilter {

    @Override
    protected boolean include(PropertyWriter writer) {
        Class entityClass = ClassUtil.getRealClass(((BeanPropertyWriter) writer).getMember().getDeclaringClass());
        return BaseBusEntity.class != entityClass && isDisplay(writer.getName(), entityClass);
    }

}
