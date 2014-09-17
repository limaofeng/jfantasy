package com.fantasy.framework.util.web.context;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 应用的上下文 提供给Action使用
 */
@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
public class ActionContext implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String CONTAINER = "_CONTAINER";

	public static final String RESULT_INFO = "_RESULT_INFO";

	private static ThreadLocal<ActionContext> threadLocal = new ThreadLocal<ActionContext>();

	private static final String LOCALE = "fantasy.locale";

	private static final String HTTP_REQUEST = "fantasy.http.request";

	private static final String HTTP_RESPONSE = "fantasy.http.response";

	private static final String PARAMETERS = "fantasy.map.parameters";

	private static final String SESSION = "fantasy.map.session";

	private static final String APPLICATION = "fantasy.map.application";

	private static final String SERVLET_CONTEXT = "fantasy.servlet.context";

	private static final String REQUEST = "fantasy.map.request";

	private static final String CONVERSION_ERRORS = "fantasy.conversion.errors";

	private Map<String, Object> context;

	public static ActionContext getContext(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> contextMap = (Map<String, Object>) request.getAttribute(ActionContext.CONTAINER);
		if (contextMap == null) {
			contextMap = createContextMap(request, response, request.getSession().getServletContext());
		}
		request.setAttribute(ActionContext.CONTAINER, contextMap);
		ActionContext context = new ActionContext(contextMap);
		ActionContext.setContext(context);
		context.put(HTTP_REQUEST, request);
		context.put(HTTP_RESPONSE, response);
		return context;
	}

	public static Map<String, Object> createContextMap(HttpServletRequest request, HttpServletResponse response, ServletContext context) {
		Map requestMap = new RequestMap(request);
		Map params = new ParameterMap(request);
		Map session = new SessionMap(request);
		Map application = new ApplicationMap(context);
		Map<String, Object> extraContext = createContextMap(requestMap, params, session, application, request, response, context);
		return extraContext;
	}

	private static HashMap<String, Object> createContextMap(Map requestMap, Map parameterMap, Map sessionMap, Map applicationMap, HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) {
		HashMap<String, Object> extraContext = new HashMap<String, Object>();

		extraContext.put(PARAMETERS, parameterMap);
		extraContext.put(SESSION, sessionMap);
		extraContext.put(APPLICATION, applicationMap);

		Locale locale = request.getLocale();

		extraContext.put(ActionContext.LOCALE, locale);

		extraContext.put(HTTP_REQUEST, request);
		extraContext.put(HTTP_RESPONSE, response);
		extraContext.put(SERVLET_CONTEXT, servletContext);

		// helpers to get access to request/session/application scope
		extraContext.put(REQUEST, requestMap);
		extraContext.put(SESSION, sessionMap);
		extraContext.put(APPLICATION, applicationMap);
		extraContext.put(PARAMETERS, parameterMap);

		AttributeMap attrMap = new AttributeMap(extraContext);

		extraContext.put("fantasy.actioncontext.attr", attrMap);
		return extraContext;
	}

	private static ServletContext getServletContext(HttpServletRequest request) {
		return request.getSession().getServletContext();
	}

	public ActionContext(Map context) {
		this.context = context;
	}

	/**
	 * 方法名称: setContext 描述: 设置本次请求的上下文
	 * 
	 * @param context
	 */
	private static void setContext(ActionContext context) {
		threadLocal.set(context);
	}

	/**
	 * 获取 Action 上下
	 * 
	 * @return
	 */
	public static ActionContext getContext() {
		ActionContext context = (ActionContext) threadLocal.get();
		return context;
	}

	/**
	 * 设置程序异常
	 * 
	 * @param conversionErrors
	 */
	public void setActionErrors(Map conversionErrors) {
		put(CONVERSION_ERRORS, conversionErrors);
	}

	/**
	 * 获得程序异常 列表
	 * 
	 * @return
	 */
	public Map getActionErrors() {
		Map errors = (Map) get(CONVERSION_ERRORS);
		if (errors == null) {
			errors = new HashMap();
			setActionErrors(errors);
		}
		return errors;
	}

	/**
	 * 获取request中的Parameters
	 * 
	 * @param parameters
	 */
	public Map<String, Object> getParameters() {
		return (Map<String, Object>) get(PARAMETERS);
	}

	/**
	 * 获取 ActionSession
	 * 
	 * @return
	 */
	public Map<String, Object> getSession() {
		return (Map<String, Object>) get(SESSION);
	}

	public Map<String, Object> getRequest() {
		return (Map<String, Object>) get(REQUEST);
	}

	public Map<String, Object> getApplication() {
		return (Map<String, Object>) get(APPLICATION);
	}

	public Object get(Object key) {
		return context.get(key);
	}

	public void put(String key, Object value) {
		context.put(key, value);
	}

	public String getIpAddr() {
		HttpServletRequest request = getHttpRequest();
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public HttpServletRequest getHttpRequest() {
		return (HttpServletRequest) context.get(HTTP_REQUEST);
	}

	public HttpServletResponse getHttpResponse() {
		return (HttpServletResponse) context.get(HTTP_RESPONSE);
	}

}
