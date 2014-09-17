package com.fantasy.security.web.savedrequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.AnyRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.web.WebUtil;

/**
 * 替换默认的HttpSessionRequestCache
 * 
 * @功能描述 ajax请求的时候不要记录请求地址
 * @author 李茂峰
 * @since 2013-9-10 上午9:07:21
 * @version 1.0
 */
public class FantasyHttpSessionRequestCache extends HttpSessionRequestCache {

	static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST";

	private PortResolver portResolver = new PortResolverImpl();
	private boolean createSessionAllowed = true;
	private RequestMatcher requestMatcher = new AnyRequestMatcher();

	@Override
	public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		if (requestMatcher.matches(request)) {
			DefaultSavedRequest savedRequest = new DefaultSavedRequest(request, portResolver);
			if (createSessionAllowed || request.getSession(false) != null) {
				request.getSession().setAttribute(SAVED_REQUEST, savedRequest);
				logger.debug("DefaultSavedRequest added to Session: " + savedRequest);
			}
			if (WebUtil.isAjax(request)) {
				String refererUrl = (String) savedRequest.getHeaderValues("Referer").get(0);
				if (ObjectUtil.isNotNull(refererUrl)) {
					ClassUtil.setValue(savedRequest, "requestURL", refererUrl);
					ClassUtil.setValue(savedRequest, "requestURI", refererUrl.replaceFirst(WebUtil.getRequestUrl(request, ""), ""));
					// ClassUtil.setValue(savedRequest, "scheme", WebUtil.getScheme(refererUrl));
					// ClassUtil.setValue(savedRequest, "serverPort", WebUtil.getPort(refererUrl));
					String[] refererUrls = refererUrl.split(StringUtil.nullValue(ClassUtil.getValue(savedRequest, "contextPath")));
					if (refererUrls.length > 1) {
						refererUrl = refererUrls[1];
						ClassUtil.setValue(savedRequest, "servletPath", refererUrl);
						ClassUtil.setValue(savedRequest, "requestURI", StringUtil.nullValue(ClassUtil.getValue(savedRequest, "contextPath")) + refererUrl);
					}
				}
				System.out.println(savedRequest);
			}
		} else {
			logger.debug("Request not saved as configured RequestMatcher did not match");
		}
	}

}
