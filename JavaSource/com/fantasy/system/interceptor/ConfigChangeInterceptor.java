package com.fantasy.system.interceptor;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.SchedulingTaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fantasy.file.FileManager;
import com.fantasy.file.service.FileManagerFactory;
import com.fantasy.framework.freemarker.FreeMarkerTemplateUtils;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fantasy.system.service.ConfigService;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Component
@Aspect
@Lazy(false)
public class ConfigChangeInterceptor implements InitializingBean {

	private static final Logger logger = Logger.getLogger(ConfigChangeInterceptor.class);

	@Resource
	private transient Configuration configuration;

	private Runnable runJavaScript = null;

	public void afterPropertiesSet() throws Exception {
		runJavaScript = new Runnable() {
			public void run() {
				try {
					ConfigChangeInterceptor.this.javaScript();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		};
	}

	@After("execution(public * com.fantasy.system.service.ConfigService.save(..))")
	public void onSaveOrUpdate(JoinPoint point) {
		this.runJavaScript();
	}

	@After("execution(public * com.fantasy.system.service.ConfigService.delete*(..))")
	public void onDelete(JoinPoint point) {
		this.runJavaScript();
	}

	public void runJavaScript() {
		SchedulingTaskExecutor executor = SpringContextUtil.getBean("spring.executor", SchedulingTaskExecutor.class);
		executor.execute(runJavaScript);
	}

	@Scheduled(fixedDelay = 1000 * 60 * 60 * 24)
	public void javaScript() throws IOException {
		ConfigService configService = SpringContextUtil.getBeanByType(ConfigService.class);
		Template template = this.configuration.getTemplate("com/fantasy/system/interceptor/template/config.ftl");
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("types", JSON.serialize(configService.allTypes()));
		data.put("configs", JSON.serialize(configService.allConfigs()));
		FreeMarkerTemplateUtils.writer(data, template, getFileManager().writeFile("/static/js/config.js"));
	}

	@SuppressWarnings("static-access")
	private FileManager getFileManager() {
		FileManager fileManager = FileManagerFactory.getWebRootFileManager();
		while (fileManager == null) {
			try {
				Thread.currentThread().sleep(TimeUnit.SECONDS.toMillis(100));
			} catch (InterruptedException e) {
				logger.error(e.getMessage(), e);
			}
			fileManager = FileManagerFactory.getWebRootFileManager();
		}
		return fileManager;
	}

}
