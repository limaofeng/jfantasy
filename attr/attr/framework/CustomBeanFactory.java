package org.jfantasy.attr.framework;

import org.jfantasy.attr.storage.bean.Attribute;
import org.jfantasy.attr.storage.bean.AttributeType;
import org.jfantasy.attr.storage.bean.AttributeVersion;
import org.jfantasy.framework.util.ognl.OgnlUtil;

/**
 * 自定义表单
 */
public interface CustomBeanFactory {

    Class loadVersion(AttributeVersion version);

    void removeVersion(AttributeVersion version);

    OgnlUtil getOgnlUtil(AttributeType attributeType);

    <T extends DynaBean> T makeDynaBean(Class<T> clazz, String number);

    DynaBean makeDynaBean(String className, String number);

    DynaBean makeDynaBean(DynaBean dynaBean);

    Attribute getAttribute(Class<?> entityClass, String propertyName);
}
