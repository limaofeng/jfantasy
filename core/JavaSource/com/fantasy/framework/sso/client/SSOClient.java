package com.fantasy.framework.sso.client;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Component("SSOClient")
public class SSOClient implements Filter {

    private static final Logger LOGGER = Logger.getLogger(SSOClient.class);
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
		LOGGER.debug(params.toString());
	}

	public void doFilter(ServletRequest theRequest, ServletResponse theResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) theRequest;
		HttpServletResponse response = (HttpServletResponse) theResponse;
		
		Enumeration<String> enumeration = request.getHeaderNames();
		while(enumeration.hasMoreElements()){
			String headerName=enumeration.nextElement();
			LOGGER.debug(headerName+"="+request.getAttribute(headerName)+"\n");
		}
		
		LOGGER.debug(request.getRequestURL());
		LOGGER.debug(request.getQueryString());
		LOGGER.debug(request.getRequestURI());
		LOGGER.debug("========================="+request.getSession().getId());
		LOGGER.debug("xxxxx");
		chain.doFilter(request, response);
	}

	public void destroy() {
		params.clear();
	}

}
