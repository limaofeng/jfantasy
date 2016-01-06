package org.jfantasy.common.interceptor;

import org.jfantasy.common.job.AreaJsJob;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 用于生成area.js方便js调用
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-4-25 下午06:24:06
 */
@Component
public class AreaChangeInterceptor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = Logger.getLogger(AreaChangeInterceptor.class);

    private Runnable runJavaScript = null;

    @Async
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        try {
            new AreaJsJob().execute(null);
        } catch (JobExecutionException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @After("execution(public * org.jfantasy.common.service.AreaService.save(..))")
    public void onSaveOrUpdate(JoinPoint point) {
        this.runJavaScript();
    }

    @After("execution(public * org.jfantasy.common.service.AreaService.delete(..))")
    public void onDelete(JoinPoint point) {
        this.runJavaScript();
    }

    public void runJavaScript() {
        SchedulingTaskExecutor executor = SpringContextUtil.getBeanByType(SchedulingTaskExecutor.class);
        executor.execute(runJavaScript);
    }

}
