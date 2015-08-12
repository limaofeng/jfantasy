package com.fantasy.framework.struts2.core.interceptor;

import com.fantasy.framework.struts2.core.context.ActionConstants;
import com.fantasy.framework.util.web.WebUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ValidationAware;
import com.opensymphony.xwork2.config.entities.ExceptionMappingConfig;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.interceptor.ExceptionHolder;
import com.opensymphony.xwork2.util.logging.Logger;
import com.opensymphony.xwork2.util.logging.LoggerFactory;
import org.apache.struts2.ServletActionContext;

import java.util.List;

public class ExceptionMappingInterceptor extends AbstractInterceptor {

	private static final long serialVersionUID = 3385801029978620607L;

	protected static final Logger LOG = LoggerFactory.getLogger(ExceptionMappingInterceptor.class);

	protected transient Logger categoryLogger;
	protected boolean logEnabled = false;
	protected String logCategory;
	protected String logLevel;

	public boolean isLogEnabled() {
		return logEnabled;
	}

	public void setLogEnabled(boolean logEnabled) {
		this.logEnabled = logEnabled;
	}

	public String getLogCategory() {
		return logCategory;
	}

	public void setLogCategory(String logCatgory) {
		this.logCategory = logCatgory;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String result;
		try {
			result = invocation.invoke();
		} catch (Exception e) {
			if (isLogEnabled()) {
				handleLogging(e);
			}
            if (WebUtil.isAjax(ServletActionContext.getRequest())) {
				Object action = invocation.getAction();
				if (action instanceof ValidationAware) {
					ValidationAware aware = (ValidationAware) action;
					aware.addActionError(e.getMessage());
				}
				LOG.error(e.toString(), e);
				result = ActionConstants.JSONDATA;
			} else {
				List<ExceptionMappingConfig> exceptionMappings = invocation.getProxy().getConfig().getExceptionMappings();
				String mappedResult = this.findResultFromExceptions(exceptionMappings, e);
				if (mappedResult != null) {
					result = mappedResult;
					publishException(invocation, new ExceptionHolder(e));
				} else {
					throw e;
				}
			}
		}
		return result;
	}

	protected void handleLogging(Exception e) {
		if (logCategory != null) {
			if (categoryLogger == null) {
				categoryLogger = LoggerFactory.getLogger(logCategory);
			}
			doLog(categoryLogger, e);
		} else {
			doLog(LOG, e);
		}
	}

	protected void doLog(Logger logger, Exception e) {
		if (logLevel == null) {
			logger.debug(e.getMessage(), e);
			return;
		}
		if ("trace".equalsIgnoreCase(logLevel)) {
			logger.trace(e.getMessage(), e);
		} else if ("debug".equalsIgnoreCase(logLevel)) {
			logger.debug(e.getMessage(), e);
		} else if ("info".equalsIgnoreCase(logLevel)) {
			logger.info(e.getMessage(), e);
		} else if ("warn".equalsIgnoreCase(logLevel)) {
			logger.warn(e.getMessage(), e);
		} else if ("error".equalsIgnoreCase(logLevel)) {
			logger.error(e.getMessage(), e);
		} else if ("fatal".equalsIgnoreCase(logLevel)) {
			logger.fatal(e.getMessage(), e);
		} else {
			throw new IllegalArgumentException("LogLevel [" + logLevel + "] is not supported");
		}
	}

	protected String findResultFromExceptions(List<ExceptionMappingConfig> exceptionMappings, Throwable t) {
		String result = null;
		if (exceptionMappings != null) {
			int deepest = Integer.MAX_VALUE;
			for (Object exceptionMapping : exceptionMappings) {
				ExceptionMappingConfig exceptionMappingConfig = (ExceptionMappingConfig) exceptionMapping;
				int depth = getDepth(exceptionMappingConfig.getExceptionClassName(), t);
				if (depth >= 0 && depth < deepest) {
					deepest = depth;
					result = exceptionMappingConfig.getResult();
				}
			}
		}

		return result;
	}

	public int getDepth(String exceptionMapping, Throwable t) {
		return getDepth(exceptionMapping, t.getClass(), 0);
	}

	@SuppressWarnings({ "rawtypes" })
	private int getDepth(String exceptionMapping, Class exceptionClass, int depth) {
		if (exceptionClass.getName().contains(exceptionMapping)) {
			return depth;
		}
		if (exceptionClass.equals(Throwable.class)) {
			return -1;
		}
		return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
	}

	protected void publishException(ActionInvocation invocation, ExceptionHolder exceptionHolder) {
		invocation.getStack().push(exceptionHolder);
	}
}