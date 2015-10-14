package com.fantasy.framework.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public interface IClass<T> {
    Property getProperty(String paramString);

    Property[] getPropertys();

    T newInstance();

    T newInstance(Object paramObject);

    T newInstance(Class<?> paramClass, Object paramObject);

    MethodProxy getMethod(String paramString);

    MethodProxy getMethod(String paramString, Class<?>[] paramArrayOfClass);

    void setValue(Object paramObject1, String paramString, Object paramObject2);

    Object getValue(Object paramObject, String paramString);

    T newInstance(Class<?>[] paramArrayOfClass, Object[] paramArrayOfObject);

    Field[] getDeclaredFields();

    Field getDeclaredField(String fieldName);

    Field[] getDeclaredFields(Class<? extends Annotation> annotClass);
}