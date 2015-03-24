package com.fantasy.framework.struts2;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.WebApplicationContext;

public abstract class StrutsSpringJUnit4TestCase<T> extends StrutsJUnit4TestCase<T> implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Override
    protected void setupBeforeInitDispatcher() throws Exception {
        this.servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}