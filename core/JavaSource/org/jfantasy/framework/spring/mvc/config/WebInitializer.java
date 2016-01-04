package org.jfantasy.framework.spring.mvc.config;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.springframework.core.annotation.Order;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

/*
 * 所有实现了WebApplicationInitializer接口的类都会在容器启动时自动被加载运行，用@Order注解设定加载顺序
 * 这是servlet3.0+后加入的特性，web.xml中可以不需要配置内容，都硬编码到WebApplicationInitializer的实现类中
 */
@Order(3)
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    /*
     * DispatcherServlet的映射路径
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    /*
      * 应用上下文，除web部分
	  */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class[] getRootConfigClasses() {
        return new Class[]{AppConfig.class, SpringSecurityConfig.class};
    }

    /*
      * web上下文
	  */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    protected Class[] getServletConfigClasses() {
        return new Class[]{WebMvcConfig.class};
    }

    /*
      * 注册过滤器，映射路径与DispatcherServlet一致，路径不一致的过滤器需要注册到另外的WebApplicationInitializer中
	  */
    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);
        return new Filter[]{characterEncodingFilter};
    }

    /*
      * 可以注册DispatcherServlet的初始化参数，等等
	  */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        //registration.setInitParameter("spring.profiles.active", "default");
    }

    /*
     * 创建一个可以在非spring管理bean中获得spring管理的相关bean的对象
     */
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        WebApplicationContext ctx = super.createRootApplicationContext();
        SpringContextUtil.setApplicationContext(ctx);
        return ctx;
    }

}
