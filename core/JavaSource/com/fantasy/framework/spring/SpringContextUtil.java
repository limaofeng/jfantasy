package com.fantasy.framework.spring;

import com.fantasy.framework.util.common.ObjectUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

    public static final int AUTOWIRE_NO = 0;
    public static final int AUTOWIRE_BY_NAME = 1;
    public static final int AUTOWIRE_BY_TYPE = 2;
    public static final int AUTOWIRE_CONSTRUCTOR = 3;
    public static final int AUTOWIRE_AUTODETECT = 4;

    private static final Log logger = LogFactory.getLog(SpringContextUtil.class);

    private static ApplicationContext applicationContext; // Spring应用上下文环境

    /**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext applicationContext
     * @throws BeansException
     */
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug(applicationContext);
        if (ObjectUtil.isNull(SpringContextUtil.applicationContext)) {
            SpringContextUtil.applicationContext = applicationContext;
        }
    }

    /**
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name beanId
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
    public static synchronized Object getBean(String name) {
        try {
            return applicationContext.getBean(name);
        } catch (NoSuchBeanDefinitionException e) {
            if (logger.isErrorEnabled()) {
                logger.error("{Bean:" + name + "}没有找到!", e);
            }
            return null;
        } catch (BeansException e) {
            if (logger.isErrorEnabled()) {
                logger.error("{Bean:" + name + "}没有找到!", e);
            }
            throw e;
        }
    }

    /**
     * 获取类型为requiredType的对象 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     *
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     * @throws BeansException
     */
    public static synchronized <T> T getBean(String name, Class<T> requiredType) {
        try {
            return applicationContext.getBean(name, requiredType);
        } catch (NoSuchBeanDefinitionException e) {
            if (logger.isErrorEnabled()) {
                logger.error("{Bean:" + name + ",Class:" + requiredType + "}没有找到!");
            }
            return null;
        } catch (BeansException e) {
            if (logger.isErrorEnabled()) {
                logger.error("{Bean:" + name + ",Class:" + requiredType + "}没有找到!");
            }
            throw e;
        } catch (NullPointerException e) {
            logger.error("查找Bean:" + name + "时发现applicationContext未启动");
            return null;
        }
    }

    public static synchronized <T> T autowireBean(T existingBean) {
        applicationContext.getAutowireCapableBeanFactory().autowireBean(existingBean);
        return existingBean;
    }

    /**
     * 由spring容器初始化该对象
     *
     * @param <T>
     * @param beanClass
     * @param autoType
     * @return
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_AUTODETECT
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T autowire(Class<T> beanClass, int autoType) {
        return (T) applicationContext.getAutowireCapableBeanFactory().autowire(beanClass, autoType, false);
    }

    /**
     * spring 创建该Bean
     *
     * @param <T>
     * @param beanClass
     * @param autoType
     * @return
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_AUTODETECT
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T> T createBean(Class<T> beanClass, int autoType) {
        return (T) applicationContext.getAutowireCapableBeanFactory().createBean(beanClass, autoType, false);
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
    public static synchronized boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    /**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
    public static synchronized boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.isSingleton(name);
    }

    /**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     */
    public static synchronized Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getType(name);
    }

    /**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
    public static synchronized String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return applicationContext.getAliases(name);
    }

    public static synchronized <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }

    public static synchronized <T> String[] getBeanNamesForType(Class<T> clazz) {
        return applicationContext.getBeanNamesForType(clazz);
    }

    public static <T> T getBeanByType(Class<T> clazz) {
        String name = ObjectUtil.first(getBeanNamesForType(clazz));
        if (name == null) {
            return null;
        }
        return getBean(name, clazz);
    }

    public static synchronized Resource[] getResources(String pattern) {
        try {
            return applicationContext.getResources(pattern);
        } catch (IOException e) {
            logger.error(e);
            return null;
        }
    }

    public void destroy() throws Exception {
        cleanApplicationContext();
    }

    public static void cleanApplicationContext() {
        applicationContext = null;
    }

    public static synchronized void registerBeanDefinition(String beanName, Class<?> clazz) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionBuilder.setAutowireMode(AUTOWIRE_BY_TYPE);
        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    public static void registerBeanDefinition(String beanName, Class<?> clazz, Object[] argValues) {
        registerBeanDefinition(beanName, clazz, argValues, new HashMap<String, Object>());
    }

    public static void registerBeanDefinition(String beanName, Class<?> clazz, Map<String, Object> propertyValues) {
        registerBeanDefinition(beanName, clazz, new Object[0], propertyValues);
    }

    public static synchronized void removeBeanDefinition(String beanName) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        defaultListableBeanFactory.removeBeanDefinition(beanName);
    }

    public static synchronized void registerBeanDefinition(String beanName, Class<?> clazz, Object[] argValues, Map<String, Object> propertyValues) {
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        beanDefinitionBuilder.setAutowireMode(AUTOWIRE_BY_TYPE);

        for (Map.Entry<String, Object> entry : propertyValues.entrySet()) {
            beanDefinitionBuilder.addPropertyValue(entry.getKey(), entry.getValue());
        }

        for (Object argValue : argValues) {
            beanDefinitionBuilder.addConstructorArgValue(argValue);
        }

        defaultListableBeanFactory.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());

    }

}