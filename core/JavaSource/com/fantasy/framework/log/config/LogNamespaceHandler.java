package com.fantasy.framework.log.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

public class LogNamespaceHandler extends NamespaceHandlerSupport {

    static final String LOG_MANAGER_ATTRIBUTE = "log-manager";
    static final String DEFAULT_LOG_MANAGER_BEAN_NAME = "logManager";

    static String extractCacheManager(Element element) {
        return element.hasAttribute(LogNamespaceHandler.LOG_MANAGER_ATTRIBUTE) ? element.getAttribute(LogNamespaceHandler.LOG_MANAGER_ATTRIBUTE) : LogNamespaceHandler.DEFAULT_LOG_MANAGER_BEAN_NAME;
    }

    static BeanDefinition parseKeyGenerator(Element element, BeanDefinition def) {
        String name = element.getAttribute("key-generator");
        if (StringUtils.hasText(name)) {
            def.getPropertyValues().add("keyGenerator", new RuntimeBeanReference(name.trim()));
        }
        return def;
    }

    public void init() {
        registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenLogBeanDefinitionParser());
    }

}
