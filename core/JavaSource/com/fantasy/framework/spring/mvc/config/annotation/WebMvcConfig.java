package com.fantasy.framework.spring.mvc.config.annotation;


import com.fantasy.framework.spring.mvc.method.annotation.FormModelMethodArgumentResolver;
import com.fantasy.framework.spring.mvc.method.annotation.PropertyFilterModelAttributeMethodProcessor;
import com.fantasy.framework.spring.mvc.method.annotation.RequestJsonParamMethodArgumentResolver;
import com.fantasy.framework.util.jackson.JSON;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.fantasy.*.rest", "com.fantasy.*.web"}, useDefaultFilters = false, includeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {Controller.class})
})
public class WebMvcConfig extends WebMvcConfigurerAdapter {

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
        registry.addResourceHandler("/static/**", "/assets/**").addResourceLocations("/static/", "/assets/");
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
        MappingJackson2HttpMessageConverter jackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        jackson2HttpMessageConverter.setObjectMapper(JSON.getObjectMapper());
        converters.add(jackson2HttpMessageConverter);
        super.configureMessageConverters(converters);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new FormModelMethodArgumentResolver());
        argumentResolvers.add(new RequestJsonParamMethodArgumentResolver());
        argumentResolvers.add(new PropertyFilterModelAttributeMethodProcessor());
        super.addArgumentResolvers(argumentResolvers);
    }

}
