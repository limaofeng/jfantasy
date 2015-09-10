package com.fantasy.framework.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

public class Property {
    private String name;
    private MethodProxy readMethodProxy;
    private MethodProxy writeMethodProxy;
    private Class<?> propertyType;
    private boolean write;
    private boolean read;

    Property(String name, MethodProxy readMethodProxy, MethodProxy writeMethodProxy, Class<?> propertyType) {
        this.read = (readMethodProxy != null);
        this.write = (writeMethodProxy != null);
        this.name = name;
        this.readMethodProxy = readMethodProxy;
        this.writeMethodProxy = writeMethodProxy;
        this.propertyType = propertyType;
    }

    public boolean isWrite() {
        return this.write;
    }

    public boolean isRead() {
        return this.read;
    }

    public Object getValue(Object target) {
        return this.readMethodProxy.invoke(target);
    }

    public void setValue(Object target, Object value) {
        this.writeMethodProxy.invoke(target, value);
    }

    public <T> Class<T> getPropertyType() {
        return (Class<T>) this.propertyType;
    }

    public String getName() {
        return this.name;
    }

    public <T extends Annotation> T getAnnotation(Class<T> tClass) {
        T annotation = null;
        if (this.isRead()) {
            annotation = this.getReadMethod().getAnnotation(tClass);
        }
        if (annotation == null && this.isWrite()) {
            annotation = this.getWriteMethod().getAnnotation(tClass);
        }
        return annotation;
    }

    public MethodProxy getReadMethod() {
        return this.readMethodProxy;
    }

    public MethodProxy getWriteMethod() {
        return this.writeMethodProxy;
    }

    public ParameterizedType getGenericType() {
        return (ParameterizedType)this.getReadMethod().getMethod().getGenericReturnType();
    }
}
