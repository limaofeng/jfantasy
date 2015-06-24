package com.fantasy.attr.framework;

import com.fantasy.attr.storage.bean.AttributeType;
import com.fantasy.attr.storage.bean.AttributeVersion;
import com.fantasy.framework.util.ognl.OgnlUtil;

/**
 * 自定义表单
 */
public interface CustomBeanFactory {

    Class loadVersion(AttributeVersion version);

    void removeVersion(AttributeVersion version);

    OgnlUtil getOgnlUtil(AttributeType attributeType);

    <T extends DynaBean> T makeDynaBean(Class<T> clazz, String number);

    DynaBean makeDynaBean(String className, String number);

}
