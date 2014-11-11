package com.fantasy.attr.util;

import com.fantasy.attr.DynaBean;
import com.fantasy.attr.bean.Attribute;
import com.fantasy.attr.bean.AttributeType;
import com.fantasy.attr.bean.AttributeValue;
import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.attr.service.AttributeVersionService;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.asm.AsmUtil;
import com.fantasy.framework.util.asm.Property;
import com.fantasy.framework.util.common.BeanUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.ognl.OgnlUtil;
import ognl.TypeConverter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class VersionUtil {

    private static final Log logger = LogFactory.getLog(VersionUtil.class);

    private static final ConcurrentMap<String, Class> dynaBeanClassCache = new ConcurrentHashMap<String, Class>();
    private static final ConcurrentMap<String, AttributeVersion> versionCache = new ConcurrentHashMap<String, AttributeVersion>();
    private static AttributeVersionService attributeVersionService;

    public static DynaBean makeDynaBean(DynaBean bean) {
        if (bean.getVersion() == null) {
            return bean;
        }
        return createDynaBean(ClassUtil.getRealClass(bean), bean.getVersion().getNumber(), bean);
    }

    public static DynaBean makeDynaBean(Class<DynaBean> clazz, String number) {
        return createDynaBean(clazz, number);
    }

    public static <T> T createDynaBean(Class<T> clazz, String number) {
        AttributeVersion version = getVersion(clazz, number);
        DynaBean dynaBean = (DynaBean) ClassUtil.newInstance(makeClass(version));
        ClassUtil.setValue(dynaBean, "version", version);
        return clazz.cast(dynaBean);
    }

    private static DynaBean createDynaBean(Class<?> clazz, String number, DynaBean bean) {
        AttributeVersion version = getVersion(clazz, number);
        DynaBean dynaBean = (DynaBean) ClassUtil.newInstance(makeClass(version));
        dynaBean.setAttributeValues(new ArrayList<AttributeValue>());
        for (Attribute attribute : version.getAttributes()) {
            AttributeValue sourceValue = ObjectUtil.find(bean.getAttributeValues(), "attribute.code", attribute.getCode());
            if (sourceValue != null && StringUtil.isNotBlank(sourceValue.getValue())) {
                getOgnlUtil(attribute.getAttributeType()).setValue(attribute.getCode(), dynaBean, sourceValue.getValue());
            }
        }
        BeanUtil.copyProperties(dynaBean, bean, "version", "attributeValues");
        return dynaBean;
    }

    public static AttributeVersion getVersion(Class<?> clazz, String number) {
        if (!versionCache.containsKey(clazz.getName() + "$v" + number)) {
            versionCache.putIfAbsent(clazz.getName() + "$v" + number, getAttributeVersionService().getVersion(clazz, number));
        }
        return versionCache.get(clazz.getName() + "$v" + number);
    }

    private synchronized static AttributeVersionService getAttributeVersionService() {
        if (attributeVersionService == null) {
            attributeVersionService = SpringContextUtil.getBeanByType(AttributeVersionService.class);
        }
        return attributeVersionService;
    }

    public static Class makeClass(Class<?> clazz, String number) {
        return makeClass(getVersion(clazz, number));
    }

    public static Class makeClass(AttributeVersion version) {
        String className = version.getClassName() + "$v" + version.getNumber();
        if (!dynaBeanClassCache.containsKey(className)) {
            String superClass = version.getClassName();
            List<Property> properties = new ArrayList<Property>();
            for (Attribute attribute : version.getAttributes()) {
                properties.add(new Property(attribute.getCode(), ClassUtil.forName(attribute.getAttributeType().getDataType())));
            }
            logger.debug("dynaBeanClass:" + className);
            dynaBeanClassCache.putIfAbsent(className, AsmUtil.makeClass(className, superClass, properties.toArray(new Property[properties.size()])));
        }
        return dynaBeanClassCache.get(className);
    }

    public static OgnlUtil getOgnlUtil(AttributeType attributeType) {
        if (!OgnlUtil.containsKey("attr-" + attributeType.getId())) {
            OgnlUtil.getInstance("attr-" + attributeType.getId()).addTypeConverter(ClassUtil.forName(attributeType.getDataType()), (TypeConverter) SpringContextUtil.createBean(ClassUtil.forName(attributeType.getConverter().getTypeConverter()), SpringContextUtil.AUTOWIRE_BY_TYPE));
        }
        return OgnlUtil.getInstance("attr-" + attributeType.getId());
    }

}
