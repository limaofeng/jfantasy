package com.fantasy.framework.log.interceptor;

import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;

import com.fantasy.framework.log.annotation.LogOperationSource;

@SuppressWarnings("serial")
public class BeanFactoryLogOperationSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	private LogOperationSource logOperationSource;

	private final LogOperationSourcePointcut pointcut = new LogOperationSourcePointcut() {
		@Override
		protected LogOperationSource getLogOperationSource() {
			return logOperationSource;
		}
	};

	public Pointcut getPointcut() {
		return pointcut;
	}

	public void setLogOperationSource(LogOperationSource logOperationSource) {
		this.logOperationSource = logOperationSource;
	}

	public void setClassFilter(ClassFilter classFilter) {
		this.pointcut.setClassFilter(classFilter);
	}
	
}
