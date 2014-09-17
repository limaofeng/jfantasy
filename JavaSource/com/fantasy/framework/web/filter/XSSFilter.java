package com.fantasy.framework.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import com.fantasy.framework.web.filter.xss.*;

import org.springframework.web.filter.GenericFilterBean;

public class XSSFilter extends GenericFilterBean {

	/**
	 * 是否转编码GET请求的参数 8859_1 => request.getCharacterEncoding()
	 */
	private boolean transform = false;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request, this.transform), response);
	}

	public void setTransform(boolean transform) {
		this.transform = transform;
	}

}
