package org.jfantasy.framework.log.annotation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

import org.jfantasy.framework.log.interceptor.LogInterceptor;

@Configuration
class ProxyLogConfiguration extends AbstractLogConfiguration {

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public LogInterceptor logInterceptor() {
        return new LogInterceptor();
    }
}