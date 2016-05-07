package org.jfantasy.framework.quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;

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

    @Override
    protected void startScheduler(Scheduler scheduler, int startupDelay) throws SchedulerException {
        super.startScheduler(scheduler, startupDelay);
    }
}
