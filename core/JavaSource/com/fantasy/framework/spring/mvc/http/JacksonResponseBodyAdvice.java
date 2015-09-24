package com.fantasy.framework.spring.mvc.http;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.spring.mvc.error.RestException;
import com.fantasy.framework.spring.mvc.http.jsonfilter.ExpendFieldsBeanPropertyFilter;
import com.fantasy.framework.spring.mvc.http.jsonfilter.NoneFieldsBeanPropertyFilter;
import com.fantasy.framework.spring.mvc.http.jsonfilter.ResultFieldsBeanPropertyFilter;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.jackson.JSON;
import com.fasterxml.jackson.databind.ser.PropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * 支持 REST 接口的响应头设置 <br/>
 * 1、X-Expend-Fields   设置需要返回的字段、在原返回字段中添加额外的字段 <br/>
 * 2、X-Result-Fields   完全由调用端控制返回的字段数量 <br/>
 * 例：X-Result-Fields：username,nickName <br/>
 */
@Order(1)
@ControllerAdvice
public class JacksonResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    private final static Log LOGGER = LogFactory.getLog(JacksonResponseBodyAdvice.class);

    public final static String X_Expansion_Fields = "X-Expansion-Fields";
    public final static String X_Page_Fields = "X-Page-Fields";
    public final static String X_Result_Fields = "X-Result-Fields";
    public final static String X_Expend_Fields = "X-Expend-Fields";

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        Class returnType = methodParameter.getMethod().getReturnType();
        return Object.class.isAssignableFrom(returnType);
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        Object returnValue = obj;
        HttpServletRequest request = ((ServletServerHttpRequest) serverHttpRequest).getServletRequest();
        final String xExpansionFields = request.getHeader(X_Expansion_Fields);//如果需要返回
        if (mediaType.isCompatibleWith(MediaType.APPLICATION_JSON)) {
            Class returnType = methodParameter.getMethod().getReturnType();

            if (Pager.class.isAssignableFrom(returnType) && !"true".equalsIgnoreCase(request.getHeader(X_Page_Fields))) {
                returnValue = ((Pager) returnValue).getPageItems();
            }

            if (isCustomFilter(request)) {
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(returnValue);
                SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
                filter.addFilter(JSON.CUSTOM_FILTER, getPropertyFilter(request));
                mappingJacksonValue.setFilters(filter);
                return mappingJacksonValue;
            } else {
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(returnValue);
                SimpleFilterProvider filter = new SimpleFilterProvider().setFailOnUnknownId(false);
                mappingJacksonValue.setFilters(filter);
                filter.addFilter(JSON.CUSTOM_FILTER, new NoneFieldsBeanPropertyFilter());
                return mappingJacksonValue;
            }

        }
        return obj;
    }


    public static boolean isCustomFilter(HttpServletRequest request) {
        return StringUtil.isNotBlank(request.getHeader(X_Result_Fields)) || StringUtil.isNotBlank(request.getHeader(X_Expend_Fields));
    }

    public static PropertyFilter getPropertyFilter(HttpServletRequest request) {
        String xResultFields = request.getHeader(X_Result_Fields);
        String xExpendFields = request.getHeader(X_Expend_Fields);
        if (StringUtil.isNotBlank(xResultFields)) {
            return new ResultFieldsBeanPropertyFilter(StringUtil.tokenizeToStringArray(xResultFields));
        }
        if (StringUtil.isNotBlank(xExpendFields)) {
            return new ExpendFieldsBeanPropertyFilter(StringUtil.tokenizeToStringArray(xExpendFields));
        }
        throw new RestException("不能初始化 BeanPropertyFilter 对象");
    }


}