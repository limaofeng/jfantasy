package com.fantasy.framework.quartz;

import org.springframework.beans.factory.annotation.Autowired;

public class FantasySchedulerFactoryBean extends org.springframework.scheduling.quartz.SchedulerFactoryBean {

    @Autowired
    private JobBeanJobFactory jobBeanJobFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setJobFactory(this.jobBeanJobFactory);
        super.afterPropertiesSet();
    }

}
