package org.jfantasy.framework.spring.mvc.method.annotation;

import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.spring.mvc.error.RestException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public class PagerModelAttributeMethodProcessor extends FormModelMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return Pager.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    protected String getParameterName(MethodParameter parameter) {
        return parameter.getParameterName();
    }

    protected Object createAttribute(String attributeName, MethodParameter parameter, WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {
        String value = getRequestValueForAttribute(attributeName, request);
        if (value != null) {
            Object attribute = createAttributeFromRequestValue(value, attributeName, parameter, binderFactory, request);
            if (attribute != null) {
                return attribute;
            }
        }
        Class<?> parameterType = parameter.getParameterType();
        if (parameterType.isArray() || List.class.isAssignableFrom(parameterType)) {
            return new Pager();
        }
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    protected void bindRequestParameters(ModelAndViewContainer mavContainer, WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest request, MethodParameter parameter) throws Exception {
        ServletRequest servletRequest = prepareServletRequest(binder.getTarget(), request, parameter);
        Pager target = (Pager) binder.getTarget();
        for (String paramName : servletRequest.getParameterMap().keySet()) {
            String value = servletRequest.getParameter(paramName);
            if (StringUtil.isBlank(value)) {
                continue;
            }
            if ("limit".equalsIgnoreCase(paramName)) {
                Integer[] limits = new Integer[0];
                for (String s : StringUtil.tokenizeToStringArray(value)) {
                    limits = ObjectUtil.join(limits, Integer.valueOf(s));
                }
                if (limits.length > 2) {
                    throw new RestException(" limit 参数格式不正确,格式为: limit=0,5 or limit=5");
                }
                if (limits.length == 1) {
                    limits = new Integer[]{0, limits[0]};
                }
                target.setFirst(limits[0]);
                target.setPageSize(limits[1]);
            } else if ("page".equalsIgnoreCase(paramName)) {
                target.setCurrentPage(Integer.valueOf(value));
            } else if ("per_page".equalsIgnoreCase(paramName)) {
                target.setPageSize(Integer.valueOf(value));
            } else if ("sort".equalsIgnoreCase(paramName)) {
                target.setOrderBy(value);
            } else if ("order".equalsIgnoreCase(paramName)) {
                target.setOrder(value);
            }
        }
    }

    private ServletRequest prepareServletRequest(Object target, NativeWebRequest request, MethodParameter parameter) {
        HttpServletRequest nativeRequest = (HttpServletRequest) request.getNativeRequest();
        MultipartRequest multipartRequest = WebUtils.getNativeRequest(nativeRequest, MultipartRequest.class);
        MockHttpServletRequest mockRequest = null;
        if (multipartRequest != null) {
            MockMultipartHttpServletRequest mockMultipartRequest = new MockMultipartHttpServletRequest();
            mockMultipartRequest.getMultiFileMap().putAll(multipartRequest.getMultiFileMap());
        } else {
            mockRequest = new MockHttpServletRequest();
        }
        assert mockRequest != null;
        for (Map.Entry<String, String> entry : getUriTemplateVariables(request).entrySet()) {
            String parameterName = entry.getKey();
            String value = entry.getValue();
            if (isPagerModelAttribute(parameterName, getModelNames())) {//TODO "true".equalsIgnoreCase(request.getHeader(JacksonResponseBodyAdvice.X_Page_Fields)))
                mockRequest.setParameter(parameterName, value);
            }
        }
        for (Map.Entry<String, String[]> entry : nativeRequest.getParameterMap().entrySet()) {
            String parameterName = entry.getKey();
            String[] value = entry.getValue();
            if (isPagerModelAttribute(parameterName, getModelNames())) {//TODO "true".equalsIgnoreCase(request.getHeader(JacksonResponseBodyAdvice.X_Page_Fields))
                mockRequest.setParameter(parameterName, value);
            }
        }
        return mockRequest;
    }

    private String[] getModelNames() {
        return new String[]{"page", "size", "per_page", "sort", "order","limit"};
    }

    private boolean isPagerModelAttribute(String parameterName, String[] modelNames) {
        for (String modelName : modelNames) {
            if (parameterName.equalsIgnoreCase(modelName)) {
                return true;
            }
        }
        return false;
    }

}
