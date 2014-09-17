package com.fantasy.framework.log.interceptor;

import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;

@SuppressWarnings("serial")
public class LogProxyFactoryBean extends AbstractSingletonProxyFactoryBean {
	private final LogInterceptor logInterceptor = new LogInterceptor();
	private Pointcut pointcut;

	public void setPointcut(Pointcut pointcut) {
		this.pointcut = pointcut;
	}

	@Override
	protected Object createMainInterceptor() {
		this.logInterceptor.afterPropertiesSet();
		if (this.pointcut != null) {
			return new DefaultPointcutAdvisor(this.pointcut, this.logInterceptor);
		} else {
			throw new UnsupportedOperationException();
		}
	}

}
