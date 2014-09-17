package com.fantasy.framework.struts2.views.freemarker;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.opensymphony.xwork2.inject.Inject;
import freemarker.template.Configuration;

import javax.servlet.ServletContext;

public class FantasyFreemarkerManager extends org.apache.struts2.views.freemarker.FreemarkerManager {

    private static final String FANTASY_FREEMARKER_BEANNAME = "fantasy.freemarker.beanName";

    private Mode mode;

    private String freemarkerBeanName;

    @Inject(FANTASY_FREEMARKER_BEANNAME)
    public synchronized void setFreemarkerBeanName(String freemarkerBeanName) {
        this.freemarkerBeanName = freemarkerBeanName;
    }

    @Override
    public synchronized Configuration getConfiguration(ServletContext servletContext) {
        if (config == null) {
            if (StringUtil.isNotBlank(freemarkerBeanName)) {
                mode = Mode.spring;
                config = SpringContextUtil.getBean(this.freemarkerBeanName, freemarker.template.Configuration.class);
                super.wrapper = createObjectWrapper(servletContext);
                config.setObjectWrapper(super.wrapper);
            } else {
                mode = Mode.struts2;
                config = super.getConfiguration(servletContext);
            }
        }
        return config;
    }

    public Mode getMode() {
        return this.mode;
    }

    protected static enum Mode {
        spring, struts2
    }

}
