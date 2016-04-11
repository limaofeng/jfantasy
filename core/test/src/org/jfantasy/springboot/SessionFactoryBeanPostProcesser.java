package org.jfantasy.springboot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class SessionFactoryBeanPostProcesser implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (HibernateJpaAutoConfiguration.class.isAssignableFrom(bean.getClass())) {
            HibernateJpaAutoConfiguration configuration = (HibernateJpaAutoConfiguration) bean;
        }
        if (EntityManagerFactory.class.isAssignableFrom(bean.getClass())) {
            HibernateJpaAutoConfiguration configuration = (HibernateJpaAutoConfiguration) bean;
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
