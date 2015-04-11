package com.fantasy.attr.framework.util;

import com.fantasy.attr.framework.DynaBean;
import com.fantasy.attr.storage.bean.Attribute;
import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.AttributeValue;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.attr.storage.service.AttributeVersionService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

public class VersionUtil {

    private static final Log logger = LogFactory.getLog(VersionUtil.class);

    private static AttributeVersionService attributeVersionService;

    public static DynaBean makeDynaBean(DynaBean bean) {
        if (bean.getVersion() == null) {
            return bean;
        }
        DynaBean dynaBean = makeDynaBean(bean.getVersion().getTargetClassName(), bean.getVersion().getNumber());
        return createDynaBean(dynaBean, bean);
    }

    public static DynaBean makeDynaBean(String className, String number) {
        AttributeVersion version = getVersion(className, number);
        return createDynaBean(ClassUtil.forName(version.getClassName()), version);
    }

    public static <T> T createDynaBean(Class<T> clazz, String number) {
        AttributeVersion version = getVersion(clazz.getName(), number);
        return clazz.cast(createDynaBean(ClassUtil.forName(version.getClassName()), version));
    }

    public static DynaBean createDynaBean(Class clazz, AttributeVersion version) {
        DynaBean dynaBean = newInstance(clazz);
        dynaBean.setVersion(version);
        return dynaBean;
    }

    private static DynaBean newInstance(Class clazz) {
        return (DynaBean) ClassUtil.newInstance(clazz);
    }

    private static DynaBean createDynaBean(DynaBean dynaBean, DynaBean bean) {
        dynaBean.setAttributeValues(new ArrayList<AttributeValue>());
        List<AttributeValue> attributeValue = StringUtil.isBlank(bean.getAttributeValueStore()) ? bean.getAttributeValues() : JSON.deserialize(bean.getAttributeValueStore(), new TypeReference<List<AttributeValue>>() {
        });
        for (Attribute attribute : bean.getVersion().getAttributes()) {
            AttributeValue sourceValue = ObjectUtil.find(attributeValue, "attribute.code", attribute.getCode());
            if (sourceValue != null && StringUtil.isNotBlank(sourceValue.getValue())) {
                getOgnlUtil(attribute.getAttributeType()).setValue(attribute.getCode(), dynaBean, sourceValue.getValue());
            }
        }
        BeanUtil.copyProperties(dynaBean, bean, "version", "attributeValues");
        return dynaBean;
    }

    public static AttributeVersion getVersion(String className, String number) {
        AttributeVersion version = getAttributeVersionService().findUniqueByTargetClassName(className, number);
        if (version == null) {
            logger.error("未找到" + className + "对应的版本[" + number + "]信息");
            return null;
        }
        return version;
    }

    private synchronized static AttributeVersionService getAttributeVersionService() {
        if (attributeVersionService == null) {
            attributeVersionService = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        }
        return attributeVersionService;
    }

    public static Class makeClass(Class<?> clazz, String number) {
        return ClassUtil.forName(getVersion(clazz.getName(), number).getClassName());
    }

    public static OgnlUtil getOgnlUtil(AttributeType attributeType) {
        if (!OgnlUtil.containsKey("attr-" + attributeType.getId())) {
            OgnlUtil.getInstance("attr-" + attributeType.getId()).addTypeConverter(ClassUtil.forName(attributeType.getDataType()), (TypeConverter) SpringContextUtil.createBean(ClassUtil.forName(attributeType.getConverter().getTypeConverter()), SpringContextUtil.AUTOWIRE_BY_TYPE));
        }
        return OgnlUtil.getInstance("attr-" + attributeType.getId());
    }

    public static Attribute getAttribute(Class<?> entityClass, String propertyName) {
        //TODO 如果不缓存，查询时可能有性能问题
        List<AttributeVersion> versions = getAttributeVersionService().getVersions(entityClass);
        for (AttributeVersion version : versions) {
            AttributeVersion attributeVersion = VersionUtil.getVersion(entityClass.getName(), version.getNumber());
            String simpleName = propertyName.contains(".") ? propertyName.substring(0, propertyName.indexOf(".")) : propertyName;
            Attribute attribute = ObjectUtil.find(attributeVersion.getAttributes(), "code", simpleName);
            if (attribute != null) {
                com.fantasy.framework.util.reflect.Property property = ClassUtil.getProperty(ClassUtil.forName(attribute.getAttributeType().getDataType()), RegexpUtil.replaceFirst(propertyName, simpleName + ".", ""));
                if (property != null) {
                    return attribute;
                }
            }
        }
        return null;
    }
}
