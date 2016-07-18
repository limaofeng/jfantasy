package org.jfantasy.framework.dao.hibernate.converter;


import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.jackson.JSON;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;

import javax.persistence.AttributeConverter;
import java.lang.reflect.Array;

public class ArrayConverter<T> implements AttributeConverter<T[], String> {

    protected Class<T> entityClass;

    public ArrayConverter() {
        this.entityClass = ReflectionUtils.getSuperClassGenricType(ClassUtil.getRealClass(getClass()));
    }

    @Override
    public String convertToDatabaseColumn(T[] attribute) {
        if (attribute == null) {
            return null;
        }
        return JSON.serialize(attribute);
    }

    @Override
    public T[] convertToEntityAttribute(String dbData) {
        if (StringUtil.isBlank(dbData)) {
            return null;
        }
        return (T[]) JSON.deserialize(dbData, Array.newInstance(entityClass, 0).getClass());
    }

}
