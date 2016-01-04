package org.jfantasy.framework.spring.mvc.config;

import org.jfantasy.framework.util.common.PropertiesHelper;
import org.springframework.core.annotation.Order;
import org.springframework.orm.hibernate3.support.OpenSessionInViewFilter;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

@Order(1)
public class CommonInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext)throws ServletException {
		//Log4jConfigListener
		PropertiesHelper propertiesHelper = PropertiesHelper.load("props/application.properties");
		servletContext.setInitParameter("webAppRootKey", propertiesHelper.getProperty("webAppRootKey"));
		//1、Log4jConfigListener
		servletContext.setInitParameter("log4jConfigLocation", "classpath:log4j/log4j.xml");//由Sprng载入的Log4j配置文件位置
		servletContext.setInitParameter("log4jRefreshInterval", "60000");//fresh once every minutes
		servletContext.setInitParameter("log4jExposeWebAppRoot", "true");//应用是否可以通过System.getProperties(“webAppRootKey”)得到当前应用名。
		servletContext.addListener(Log4jConfigListener.class);

		//OpenSessionInViewFilter
		OpenSessionInViewFilter hibernateSessionInViewFilter = new OpenSessionInViewFilter();
		FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("hibernateOpenSessionInViewFilter", hibernateSessionInViewFilter);
		filterRegistration.setInitParameter("flushMode", "COMMIT");
		filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");

	}


}