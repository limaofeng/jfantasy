package com.fantasy.common.interceptor;

import javax.annotation.Resource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fantasy.common.bean.FtpConfig;
import com.fantasy.common.service.FtpServiceFactory;

@Component
@Aspect
public class FtpConfigSaveInterceptor {

	@Resource
	private FtpServiceFactory ftpServiceFactory;
	
	@After("execution(public * com.fantasy.common.service.FtpConfigService.save(..))")
	public void freshFtpService(JoinPoint point) {
		ftpServiceFactory.updateFtpService((FtpConfig)point.getArgs()[0]);
	}
	
}
