package com.fantasy.common.interceptor;

import com.fantasy.common.job.AreaJsJob;
import com.fantasy.framework.spring.SpringContextUtil;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.stereotype.Component;

/**
 * 用于生成area.js方便js调用
 * 
 * @author 李茂峰
 * @since 2013-4-25 下午06:24:06
 * @version 1.0
 */
@Component
public class AreaChangeInterceptor implements InitializingBean {

	private static final Logger LOGGER = Logger.getLogger(AreaChangeInterceptor.class);

	private Runnable runJavaScript = null;

	public void afterPropertiesSet() throws Exception {
		runJavaScript = new Runnable() {
			public void run() {
				try {
                    new AreaJsJob().execute(null);
				} catch (JobExecutionException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		};
	}

	@After("execution(public * com.fantasy.common.service.AreaService.save(..))")
	public void onSaveOrUpdate(JoinPoint point) {
		this.runJavaScript();
	}

	@After("execution(public * com.fantasy.common.service.AreaService.delete(..))")
	public void onDelete(JoinPoint point) {
		this.runJavaScript();
	}

	public void runJavaScript() {
		SchedulingTaskExecutor executor = SpringContextUtil.getBeanByType(SchedulingTaskExecutor.class);
		executor.execute(runJavaScript);
	}

}
