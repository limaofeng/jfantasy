package com.fantasy.framework.spring.mvc.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

//@Order(1)
//@ControllerAdvice(basePackages = "com.fantasy.*.rest")
public class MyResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private final static Log LOGGER = LogFactory.getLog(MyResponseBodyAdvice.class);
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        LOGGER.debug(">>>>");
        return methodParameter.getMethod().getReturnType().isAssignableFrom(Object.class);
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType,Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        LOGGER.debug(obj);
        return obj;
    }
}