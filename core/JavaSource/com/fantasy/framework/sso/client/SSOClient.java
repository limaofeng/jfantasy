package com.fantasy.framework.sso.client;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

@Component("SSOClient")
public class SSOClient implements Filter {

	private static final String PARAM_NAME_EXCLUDES_URL = "com.fantasy.framework.sso.excludesUrl";
	private static final String PARAM_NAME_includes_URL = "com.fantasy.framework.sso.includesUrl";
	private static final String PARAM_NAME_validate_URL = "com.fantasy.framework.sso.validateUrl";
	
	private Map<String, String> params = new HashMap<String, String>();

	public void init(FilterConfig config) throws ServletException {
		Enumeration<String> enumeration = config.getInitParameterNames();
		while (enumeration.hasMoreElements()) {
			String paramName = enumeration.nextElement();
			params.put(paramName, config.getInitParameter(paramName));
		}
		System.out.println(params.toString());
	}

	public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) theRequest;
		HttpServletResponse response = (HttpServletResponse) theResponse;
		
		Enumeration<String> enumeration = request.getHeaderNames();
		while(enumeration.hasMoreElements()){
			String headerName=enumeration.nextElement();
			System.out.println(headerName+"="+request.getAttribute(headerName)+"\n");
		}
		
		System.out.println(request.getRequestURL());
		System.out.println(request.getQueryString());
		System.out.println(request.getRequestURI());
		System.out.println("========================="+request.getSession().getId());
		System.out.println("xxxxx");
		chain.doFilter(request, response);
	}

	public void destroy() {
		params.clear();
	}

}
