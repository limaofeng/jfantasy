package com.fantasy.framework.spring.mvc.http;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Order(1)
@ControllerAdvice(basePackages = "com.fantasy.*.rest")
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        System.err.println(">>>>");
        return methodParameter.getMethod().getReturnType().isAssignableFrom(Object.class);
    }

    @Override
    public Object beforeBodyWrite(
            Object obj, MethodParameter methodParameter, MediaType mediaType,
            Class<? extends HttpMessageConverter<?>> converterType,
            ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        System.err.println(obj);
        return obj;
    }
}