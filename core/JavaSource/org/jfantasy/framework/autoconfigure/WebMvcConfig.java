package org.jfantasy.framework.autoconfigure;


import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.thetransactioncompany.cors.CORSFilter;
import org.hibernate.validator.HibernateValidator;
import org.jfantasy.framework.spring.mvc.method.annotation.FormModelMethodArgumentResolver;
import org.jfantasy.framework.spring.mvc.method.annotation.PagerModelAttributeMethodProcessor;
import org.jfantasy.framework.spring.mvc.method.annotation.PropertyFilterModelAttributeMethodProcessor;
import org.jfantasy.framework.spring.mvc.method.annotation.RequestJsonParamMethodArgumentResolver;
import org.jfantasy.framework.util.jackson.JSON;
import org.jfantasy.framework.util.web.filter.ActionContextFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.orm.hibernate4.support.OpenSessionInViewFilter;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.DispatcherType;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"org.jfantasy.*.rest"}, useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})
})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    /**
     * <基于cookie的本地化资源处理器>. <br>
     *
     * @return CookieLocaleResolver
     */
    @Bean(name = "localeResolver")
    public CookieLocaleResolver cookieLocaleResolver() {
        return new CookieLocaleResolver();
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        MappingJackson2JsonView jackson2JsonView = new MappingJackson2JsonView();
        jackson2JsonView.setJsonpParameterNames(new HashSet<String>() {
            {
                this.add("callback");
            }
        });
        registry.enableContentNegotiation();
        super.configureViewResolvers(registry);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("*.html").addResourceLocations("/");
    }

    @Bean
    public MultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(10485760);
        return multipartResolver;
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        converters.add(new MappingJackson2HttpMessageConverter(JSON.getObjectMapper()));
        super.configureMessageConverters(converters);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new FormModelMethodArgumentResolver());
        argumentResolvers.add(new RequestJsonParamMethodArgumentResolver());
        argumentResolvers.add(new PropertyFilterModelAttributeMethodProcessor());
        argumentResolvers.add(new PagerModelAttributeMethodProcessor());
        super.addArgumentResolvers(argumentResolvers);
    }

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Validator getValidator() {
        if (applicationContext instanceof XmlWebApplicationContext) {
            ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
            DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();
            BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LocalValidatorFactoryBean.class);
            beanDefinitionBuilder.setAutowireMode(2);
            beanDefinitionBuilder.addPropertyValue("providerClass", HibernateValidator.class);
            defaultListableBeanFactory.registerBeanDefinition("validator", beanDefinitionBuilder.getBeanDefinition());
            return configurableApplicationContext.getBean("validator", Validator.class);
        } else {
            return super.getValidator();
        }
    }

    @Bean
    public FilterRegistrationBean corsFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new CORSFilter());
        filterRegistrationBean.addInitParameter("cors.allowOrigin","*");
        filterRegistrationBean.addInitParameter("cors.supportedMethods","GET, POST, HEAD, PUT, DELETE, OPTIONS");
        filterRegistrationBean.addInitParameter("cors.supportedHeaders","Accept, Origin, X-Requested-With, Content-Type, Last-Modified");
        filterRegistrationBean.addInitParameter("cors.exposedHeaders","Set-Cookie");
        filterRegistrationBean.addInitParameter("cors.supportsCredentials","true");
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setOrder(100);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean actionContextFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new ActionContextFilter());
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setOrder(200);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean openSessionInViewFilter() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new OpenSessionInViewFilter());
        filterRegistrationBean.addInitParameter("flushMode", "COMMIT");
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setOrder(300);
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.setOrder(400);
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public ServletRegistrationBean druidServlet() {
        return new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
    }

    /*
    @Bean
    public FilterRegistrationBean fileFilterFilterRegistrationBean(@Qualifier("fileFilter") FileFilter fileFilter) {
        PropertiesHelper propertiesHelper = PropertiesHelper.load("props/application.properties");
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(fileFilter);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.setOrder(3);
        filterRegistrationBean.addInitParameter("allowHosts",propertiesHelper.getProperty("file.allowHosts", "static.jfantasy.org"));
        filterRegistrationBean.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.INCLUDE);
        filterRegistrationBean.addUrlPatterns("/*");
        return filterRegistrationBean;
    }*/

}
