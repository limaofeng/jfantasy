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
        Capsule capsule = stack.peek();
        Class propertyType = ((BeanPropertyWriter) writer).getPropertyType();

        if (ClassUtil.getDeclaredField(BaseBusEntity.class, name) != null) {
            return isResultFields(name, expendFields);
        }

        boolean display = isDisplay(name, entityClass) || isResultFields(name, expendFields);
        while (stack.peek() != null && stack.peek().getClazz() != entityClass) {
            stack.pop();
        }
        if (!display && capsule != null && entityClass.equals(capsule.getClazz())) {
            String _name = capsule.getName() + "." + name;
            display = isResultFields(_name, expendFields);
        }
        try {
            return display;
        } finally {
            if (display && ClassUtil.isBeanType(propertyType) && (capsule == null || capsule.getClazz() != propertyType)) {
                String prefix = capsule == null ? "" : (capsule.getName() + ".");
                stack.push(new Capsule(propertyType, prefix + name));
            }
        }
    }
}