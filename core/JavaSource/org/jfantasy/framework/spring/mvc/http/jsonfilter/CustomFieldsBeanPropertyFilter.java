package org.jfantasy.framework.spring.mvc.http.jsonfilter;

import org.jfantasy.framework.util.Stack;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public void addCapsule(String name, Class entityClass, Class propertyType) {
        if(ClassUtil.isBeanType(propertyType)) {
            Capsule capsule = stack.peek();
            if (capsule != null && capsule.getPropertyType() != entityClass) {
                while (stack.peek()!=null&&stack.peek().getPropertyType() != entityClass){
                    stack.pop();
                }
            }
            stack.push(new Capsule(name, entityClass, propertyType));
        }
    }

    public Capsule clearCapsule(Class entityClass, Class propertyType) {
        Capsule capsule = stack.peek();
        if(capsule == null){
            return null;
        }
        if (capsule.getPropertyType() != entityClass) {
            while (stack.peek()!=null&&stack.peek().getPropertyType() != entityClass){
                stack.pop();
            }
        }
        return stack.peek();
    }

    public class Capsule {
        private String name;
        private Class<?> entityClass;
        private Class<?> propertyType;
        private boolean basicType;

        public Capsule(String name, Class<?> entityClass, Class<?> propertyType) {
            this.name = name;
            this.entityClass = entityClass;
            this.propertyType = propertyType;
            this.basicType = ClassUtil.isBasicType(propertyType);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Class<?> getPropertyType() {
            return propertyType;
        }

        public void setPropertyType(Class<?> propertyType) {
            this.propertyType = propertyType;
        }

        public Class<?> getEntityClass() {
            return entityClass;
        }

        public void setEntityClass(Class<?> entityClass) {
            this.entityClass = entityClass;
        }

        public String getPrefix() {
            String prefix = "";
            List<Capsule> capsules = new ArrayList<Capsule>(stack.toList());
            Collections.reverse(capsules);
            for (Capsule capsule : capsules) {
                if(capsule.basicType){
                    continue;
                }
                prefix += (capsule.getName() + ".");
            }
            return prefix;
        }
    }
}