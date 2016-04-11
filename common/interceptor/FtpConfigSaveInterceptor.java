package org.jfantasy.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import org.jfantasy.common.bean.FtpConfig;
import org.jfantasy.common.service.FtpServiceFactory;

@Component
@Aspect
public class FtpConfigSaveInterceptor {

    @Autowired
    private FtpServiceFactory ftpServiceFactory;

    @After("execution(public * org.jfantasy.common.service.FtpConfigService.save(..))")
    public void freshFtpService(JoinPoint point) {
        ftpServiceFactory.updateFtpService((FtpConfig) point.getArgs()[0]);
    }

}
