package com.fantasy.framework.comet;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.filter.GenericFilterBean;

/**
 * java web 长连接类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-12-3 下午05:44:59
 * @version 1.0
 */
public class CometFilter extends GenericFilterBean {

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		System.out.println(request.getRequestURI());
		System.out.println(request.getRequestURL());
		
		chain.doFilter(servletRequest, servletResponse);
	}

}
