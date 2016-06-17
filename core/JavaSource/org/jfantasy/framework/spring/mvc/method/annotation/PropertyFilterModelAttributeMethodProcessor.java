package org.jfantasy.framework.spring.mvc.method.annotation;

import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.*;

public class PropertyFilterModelAttributeMethodProcessor extends FormModelMethodArgumentResolver {

    private String[] modelPrefixNames;

    {
        List<String> modelPrefixNameList = new ArrayList<String>();
        for (PropertyFilter.MatchType matchType : PropertyFilter.MatchType.values()) {
            for (PropertyFilter.PropertyType propertyType : PropertyFilter.PropertyType.values()) {
                modelPrefixNameList.add(matchType.name() + propertyType.name());
            }
        }
        modelPrefixNames = modelPrefixNameList.toArray(new String[modelPrefixNameList.size()]);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return "filters".equals(parameter.getParameterName()) && List.class.isAssignableFrom(parameter.getParameterType()) && isPropertyFilterParameter(parameter.getGenericParameterType());
    }

    private static boolean isPropertyFilterParameter(Type type) {
        if (type instanceof ParameterizedType) {
            Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
            return actualTypes.length == 1 && actualTypes[0] == PropertyFilter.class;
        }
        return false;
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
            return ArrayList.class.newInstance();
        }
        return BeanUtils.instantiateClass(parameter.getParameterType());
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return (variables != null) ? variables : Collections.<String, String>emptyMap();
    }

    protected final Map<String, String> getUriQueryVariables(NativeWebRequest request) {
        parseQuery(((ServletWebRequest) request).getRequest().getQueryString());
        return new HashMap<>();
    }

    public static Map<String, String[]> parseQuery(String query) {
        Map<String, String[]> params = new LinkedHashMap<String, String[]>();
        if (StringUtil.isBlank(query)) {
            return params;
        }
        for (String pair : query.split("[;&]")) {
            String[] vs = pair.split("=");
            String key = vs[0];
            String val = vs.length == 1 ? "" : vs[1];
            if (StringUtil.isNotBlank(val)) {
                try {
                    val = URLDecoder.decode(val, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    throw new IgnoreException(e.getMessage(), e);
                }
            }
            if (!params.containsKey(key)) {
                params.put(key, new String[]{val});
            } else {
                params.put(key, ObjectUtil.join(params.get(key), val));
            }
        }
        return params;
    }

    protected void bindRequestParameters(ModelAndViewContainer mavContainer, WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest request, MethodParameter parameter) throws Exception {
        ServletRequest servletRequest = prepareServletRequest(binder.getTarget(), request, parameter);
        @SuppressWarnings("unchecked")
        List<Object> target = (List<Object>) binder.getTarget();
        for (String paramName : servletRequest.getParameterMap().keySet()) {
            String[] values = request.getParameterValues(paramName);
            PropertyFilter.MatchType matchType = PropertyFilter.MatchType.get(paramName);
            assert matchType != null;
            if (matchType.isNone()) {
                target.add(new PropertyFilter(paramName));
            } else if (matchType.isMulti()) {
                List<String> _values = new ArrayList<String>();
                for(String val : values){
                    _values.addAll(Arrays.asList(StringUtil.tokenizeToStringArray(val)));
                }
                target.add(new PropertyFilter(paramName,_values.toArray(new String[_values.size()])));
            } else if (values.length != 0 && StringUtil.isNotBlank(values[0])) {
                target.add(new PropertyFilter(paramName, values[0]));
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
            if (isPropertyFilterModelAttribute(parameterName, modelPrefixNames)) {
                mockRequest.setParameter(parameterName, value);
            }
        }
        for (Map.Entry<String, String[]> entry : nativeRequest.getParameterMap().entrySet()) {
            String parameterName = entry.getKey();
            String[] value = entry.getValue();
            if (isPropertyFilterModelAttribute(parameterName, modelPrefixNames)) {
                mockRequest.setParameter(parameterName, value);
            }
        }
        /*
        for (Map.Entry<String, String> entry : getUriQueryVariables(request).entrySet()) {
            String parameterName = entry.getKey();
            String value = entry.getValue();
            if (isPropertyFilterModelAttribute(parameterName, modelPrefixNames)) {
                mockRequest.setParameter(parameterName, value);
            }
        }*/
        return mockRequest;
    }

    private boolean isPropertyFilterModelAttribute(String parameterName, String[] modelPrefixNames) {
        return PropertyFilter.MatchType.is(parameterName);
    }

}
