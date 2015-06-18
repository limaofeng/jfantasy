package com.fantasy.framework.spring.mvc.config;

import com.fantasy.framework.spring.ClassPathScanner;
import com.fantasy.framework.spring.mvc.config.annotation.WebMvcConfig;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.PropertiesHelper;
import com.fantasy.framework.util.web.filter.ActionContextFilter;
import com.fantasy.framework.web.filter.CharacterEncodingFilter;
import com.tacitknowledge.filters.cache.CacheHeaderFilter;
import com.tacitknowledge.filters.gzipfilter.GZIPFilter;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.util.IntrospectorCleanupListener;
import org.springframework.web.util.Log4jConfigListener;

import javax.servlet.*;
import java.util.EnumSet;

public class WebInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        servletContext.setInitParameter("webAppRootKey", PropertiesHelper.load("props/application.properties").getProperty("webAppRootKey"));
        //1、Log4jConfigListener
        servletContext.setInitParameter("log4jConfigLocation", "classpath:log4j/log4j.xml");//由Sprng载入的Log4j配置文件位置
        servletContext.setInitParameter("log4jRefreshInterval", "60000");//fresh once every minutes
        servletContext.setInitParameter("log4jExposeWebAppRoot", "true");//应用是否可以通过System.getProperties(“webAppRootKey”)得到当前应用名。
        servletContext.addListener(Log4jConfigListener.class);

        //2、.初始化spring容器
        servletContext.setInitParameter("contextConfigLocation", "classpath:spring/applicationContext.xml");//加载Spring配置文件
        servletContext.addListener(ContextLoaderListener.class);

        //3、添加 IntrospectorCleanupListener
        servletContext.addListener(IntrospectorCleanupListener.class);//Spring 刷新Introspector防止内存泄露

        //4、springmvc上下文
        AnnotationConfigWebApplicationContext springMvcContext = new AnnotationConfigWebApplicationContext();
        springMvcContext.register(WebMvcConfig.class);

        //5、DispatcherServlet
        DispatcherServlet dispatcherServlet = new DispatcherServlet(springMvcContext);
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcherServlet", dispatcherServlet);
        dynamic.setLoadOnStartup(1);
        dynamic.addMapping("/*");

        //GZIPFilter 设置自己想要压缩的文件类型
        GZIPFilter gzipFilter = new GZIPFilter();
        FilterRegistration.Dynamic filterRegistration = servletContext.addFilter("gzipFilter", gzipFilter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*.js", "*.css");

        //添加缓存 Header 设置自己想要缓存的文件类型
        CacheHeaderFilter cacheHeaderFilter = new CacheHeaderFilter();
        filterRegistration = servletContext.addFilter("cacheHeaderFilter", cacheHeaderFilter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*.gif", "*.jpg", "*.png", "*.js", "*.css");

        //图形验证码
        DelegatingFilterProxy jcaptchaFilter = new DelegatingFilterProxy();
        filterRegistration = servletContext.addFilter("jcaptchaFilter", jcaptchaFilter);
        filterRegistration.setInitParameter("targetFilterLifecycle", "true");
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/jcaptcha.jpg");

        //5、为 request, response 提供上下文访问对象
        ActionContextFilter actionContextFilter = new ActionContextFilter();
        filterRegistration = servletContext.addFilter("actionContextFilter", actionContextFilter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        //4、CharacterEncodingFilter
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("utf-8");
        filterRegistration = servletContext.addFilter("characterEncodingFilter", characterEncodingFilter);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "/*");

        //OpenSessionInViewFilter
        OpenSessionInViewFilter hibernateSessionInViewFilter = new OpenSessionInViewFilter();
        filterRegistration = servletContext.addFilter("hibernateOpenSessionInViewFilter", hibernateSessionInViewFilter);
        filterRegistration.setInitParameter("flushMode", "COMMIT");
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");

        //SpringSecurityFilterChain
        DelegatingFilterProxy springSecurityFilterChain = new DelegatingFilterProxy();
        filterRegistration = servletContext.addFilter("springSecurityFilterChain", springSecurityFilterChain);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");

        //6、FileFilter
        DelegatingFilterProxy fileFilter = new DelegatingFilterProxy();
        filterRegistration = servletContext.addFilter("fileFilter", fileFilter);
        filterRegistration.setInitParameter("targetFilterLifecycle", "true");
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");

        //依赖 weixin 模块
        if (!ClassPathScanner.getInstance().findAnnotationedClasses("com.fantasy.wx.framework.web.filter", Component.class).isEmpty()) {
            DelegatingFilterProxy weixinSessionOpenFilter = new DelegatingFilterProxy();
            filterRegistration = servletContext.addFilter("weixinSessionOpenFilter", weixinSessionOpenFilter);
            filterRegistration.setInitParameter("targetFilterLifecycle", "true");
            filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");
        }

        //struts2 相关配置
        Class<Filter> struts2Class = ClassUtil.forName("org.apache.struts2.dispatcher.ng.filter.StrutsPrepareAndExecuteFilter");
        if (struts2Class != null) {
            //Freemarker 模板中使用struts2标签
            Servlet jspSupportServlet = (Servlet) ClassUtil.newInstance("org.apache.struts2.views.JspSupportServlet");
            dynamic = servletContext.addServlet("jspSupportServlet", jspSupportServlet);
            dynamic.setLoadOnStartup(2);
            //添加struts2
            Filter struts2 = ClassUtil.newInstance(struts2Class);
            filterRegistration = servletContext.addFilter("struts2", struts2);
            filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE), false, "/*");
        }
    }
}
