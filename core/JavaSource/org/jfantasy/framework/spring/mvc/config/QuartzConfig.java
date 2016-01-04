package org.jfantasy.framework.spring.mvc.config;

import org.jfantasy.framework.quartz.FantasySchedulerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;


@Configuration
public class QuartzConfig {

    @Resource(name = "dataSource")
    public DataSource dataSource;

    @Bean(name = "scheduler")
    public SchedulerFactoryBean fantasySchedulerFactoryBean() {
        FantasySchedulerFactoryBean schedulerFactoryBean = new FantasySchedulerFactoryBean();
        schedulerFactoryBean.setAutoStartup(false);
        schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(false);
        schedulerFactoryBean.setDataSource(this.dataSource);
        schedulerFactoryBean.setOverwriteExistingJobs(false);
        schedulerFactoryBean.setTaskExecutor(new ThreadPoolTaskExecutor());
        Properties properties = new Properties();
        properties.setProperty("org.quartz.scheduler.instanceName", "QuartzScheduler");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty("org.quaartz.scheduler.wrapJobExecutionInUserTransaction", "false");
        properties.setProperty("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        properties.setProperty("org.quartz.threadPool.threadCount", "5");
        properties.setProperty("org.quartz.threadPool.threadPriority", "5");
        properties.setProperty("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.setProperty("org.quartz.jobStore.dontSetAutoCommitFalse", "false");
        properties.setProperty("org.quartz.jobStore.useProperties", "true");
        properties.setProperty("org.quartz.scheduler.dbFailureRetryInterval", "1500");
        properties.setProperty("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "60000");
        properties.setProperty("org.quartz.scheduler.rmi.proxy", "false");
        properties.setProperty("org.quartz.plugin.triggHistory.class", "org.quartz.plugins.history.LoggingTriggerHistoryPlugin");
        properties.setProperty("org.quartz.plugin.triggHistory.triggerFiredMessage", " Trigger \\{1\\}.\\{0\\} fired job \\{6\\}.\\{5\\} at: \\{4,\n date, HH:mm:ss MM/dd/yyyy}");
        properties.setProperty("org.quartz.plugin.triggHistory.triggerCompleteMessage", "Trigger \\{1\\}.\\{0\\} completed firing job \\{6\\}.\\{5\\}\n at \\{4, date, HH:mm:ss MM/dd/yyyy\\}.");
        properties.setProperty("org.quartz.plugin.shutdownhook.class", "org.quartz.plugins.management.ShutdownHookPlugin");
        properties.setProperty("org.quartz.plugin.shutdownhook.cleanShutdown", "true");
        schedulerFactoryBean.setQuartzProperties(properties);
        return schedulerFactoryBean;
    }

}
