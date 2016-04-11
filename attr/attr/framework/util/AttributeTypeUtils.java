package org.jfantasy.attr.framework.util;


import org.jfantasy.attr.framework.converter.PrimitiveTypeConverter;
import org.jfantasy.attr.storage.bean.AttributeType;
import org.jfantasy.attr.storage.service.AttributeTypeService;
import org.jfantasy.framework.spring.SpringContextUtil;

public class AttributeTypeUtils {

    private AttributeTypeUtils() {
    }

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
