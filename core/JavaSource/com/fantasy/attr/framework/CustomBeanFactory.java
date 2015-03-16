package com.fantasy.attr.framework;

import com.fantasy.attr.storage.bean.AttributeVersion;

/**
 *  自定义表单
 */
public interface CustomBeanFactory {

    public Class loadVersion(AttributeVersion version);

    public void removeVersion(AttributeVersion version);


}
