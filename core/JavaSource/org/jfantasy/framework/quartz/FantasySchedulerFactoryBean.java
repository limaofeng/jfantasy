package org.jfantasy.framework.quartz;

public class FantasySchedulerFactoryBean extends org.springframework.scheduling.quartz.SchedulerFactoryBean {

    private JobBeanJobFactory jobBeanJobFactory;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.setJobFactory(this.jobBeanJobFactory);
        super.afterPropertiesSet();
    }

    public void setJobBeanJobFactory(JobBeanJobFactory jobBeanJobFactory) {
        this.jobBeanJobFactory = jobBeanJobFactory;
    }
}
