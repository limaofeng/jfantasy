package com.fantasy.attr.framework.util;


import com.fantasy.attr.framework.converter.PrimitiveTypeConverter;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.service.ConverterService;
import com.fantasy.framework.spring.SpringContextUtil;
import org.hibernate.criterion.Restrictions;

public class AttributeTypeUtils {

    public static AttributeType primitive(String name, Class<?> javaType) {
        AttributeType attributeType = new AttributeType();
        attributeType.setName(name);
        attributeType.setDataType(javaType.getName());
        attributeType.setConverter(SpringContextUtil.getBeanByType(ConverterService.class).findUnique(Restrictions.eq("typeConverter", PrimitiveTypeConverter.class.getName())));
        attributeType.setDescription("");
        return attributeType;
    }

}
