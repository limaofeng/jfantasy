package com.fantasy.framework.comet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * java web 长连接类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-12-3 下午05:44:59
 * @version 1.0
 */
public class CometFilter extends GenericFilterBean {

    private static final Log LOGGER = LogFactory.getLog(CometFilter.class);

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		LOGGER.debug(request.getRequestURI());
		LOGGER.debug(request.getRequestURL());
		
		chain.doFilter(servletRequest, servletResponse);
	}

}
