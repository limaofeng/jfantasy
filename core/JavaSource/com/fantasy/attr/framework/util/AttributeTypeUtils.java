package com.fantasy.attr.framework.util;


import com.fantasy.attr.framework.converter.PrimitiveTypeConverter;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.service.AttributeTypeService;
import com.fantasy.framework.spring.SpringContextUtil;

public class AttributeTypeUtils {

    private static AttributeTypeService attributeTypeService;

    private static AttributeTypeService getAttributeTypeService() {
        return attributeTypeService != null ? attributeTypeService : (attributeTypeService = SpringContextUtil.getBeanByType(AttributeTypeService.class));
    }

    public static AttributeType primitive(String name, Class<?> javaType) {
        AttributeType attributeType = new AttributeType();
        attributeType.setName(name);
        attributeType.setDataType(javaType.getName());
        attributeType.setConverter(TypeConverterUtils.getTypeConverter(PrimitiveTypeConverter.class));
        attributeType.setDescription("");
        return attributeType;
    }

    public static AttributeType get(Class<?> clazz) {
        return getAttributeTypeService().findUniqueByJavaType(clazz);
    }
}
