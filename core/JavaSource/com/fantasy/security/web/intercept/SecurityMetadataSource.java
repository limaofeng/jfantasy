package com.fantasy.security.web.intercept;

import com.fantasy.framework.cache.Cache;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.security.service.ResourceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {

	private Lock lock = new ReentrantLock();

	@Resource
	private ResourceService resourceService;

	private Cache cache;

	private Map<String, RequestMatcher> requestMatchers = new ConcurrentHashMap<String, RequestMatcher>();

	public static final String LOAD_URL_AUTHORITIES_CACHE_KEY = "LOAD_URL_AUTHORITIES_CACHE_KEY";

	public void afterPropertiesSet() throws Exception {
	}

	public Collection<ConfigAttribute> getAttributes(Object filter) throws IllegalArgumentException {
		FilterInvocation filterInvocation = (FilterInvocation) filter;
		String grantedAuthorities = getGrantedAuthorities(filterInvocation.getHttpRequest());
		if (StringUtil.isNotBlank(grantedAuthorities)) {
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			for (String grantedAuthoritie : grantedAuthorities.split(",")) {
				configAttributes.add(new SecurityConfig(grantedAuthoritie));
			}
			return configAttributes;
		}
		return new ArrayList<ConfigAttribute>();
	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public String getGrantedAuthorities(HttpServletRequest request) {
		Object data = this.cache.getObject(LOAD_URL_AUTHORITIES_CACHE_KEY);
		if (ObjectUtil.isNull(data)) {
			try {
				this.lock.lock();
				data = this.cache.getObject(LOAD_URL_AUTHORITIES_CACHE_KEY);
				if (ObjectUtil.isNull(data)) {
					data = this.resourceService.loadUrlAuthorities();
					this.cache.putObject(LOAD_URL_AUTHORITIES_CACHE_KEY, data);
				}
			} finally {
				this.lock.unlock();
			}
		}
		Map<String, String> urlAuthorities = (Map<String, String>) data;
		for (Iterator<Map.Entry<String, String>> iter = urlAuthorities.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, String> entry = iter.next();
			String url = (String) entry.getKey();
			if (getRequestMatcher(url).matches(request)) {
				return entry.getValue();
			}
		}
		return "";
	}

	private RequestMatcher getRequestMatcher(String url) {
		if (!requestMatchers.containsKey(url)) {
			requestMatchers.put(url, new AntPathRequestMatcher(url));//new RegexRequestMatcher(url, null)
		}
		return requestMatchers.get(url);
	}

	public void setCache(Cache cache) {
		this.cache = cache;
	}

}