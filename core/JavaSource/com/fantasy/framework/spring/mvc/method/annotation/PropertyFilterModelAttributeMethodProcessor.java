package com.fantasy.framework.spring.mvc.method.annotation;

import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.common.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class PropertyFilterModelAttributeMethodProcessor implements HandlerMethodArgumentResolver {

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
    public final Object resolveArgument(MethodParameter parameter,
                                        ModelAndViewContainer mavContainer,
                                        NativeWebRequest request,
                                        WebDataBinderFactory binderFactory) throws Exception {
        String name = parameter.getParameterName();

        Object target = (mavContainer.containsAttribute(name)) ? mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);
        WebDataBinder binder = binderFactory.createBinder(request, target, name);
        target = binder.getTarget();
        if (target != null) {
            bindRequestParameters(mavContainer, binderFactory, binder, request, parameter);
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors()) {
                if (isBindExceptionRequired(binder, parameter)) {
                    throw new BindException(binder.getBindingResult());
                }
            }
        }
        target = binder.convertIfNecessary(binder.getTarget(), parameter.getParameterType());
        mavContainer.addAttribute(name, target);
        return target;
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

    protected String getRequestValueForAttribute(String attributeName, NativeWebRequest request) {
        Map<String, String> variables = getUriTemplateVariables(request);
        if (StringUtils.hasText(variables.get(attributeName))) {
            return variables.get(attributeName);
        } else if (StringUtils.hasText(request.getParameter(attributeName))) {
            return request.getParameter(attributeName);
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected final Map<String, String> getUriTemplateVariables(NativeWebRequest request) {
        Map<String, String> variables =
                (Map<String, String>) request.getAttribute(
                        HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST);
        return (variables != null) ? variables : Collections.<String, String>emptyMap();
    }

    protected Object createAttributeFromRequestValue(String sourceValue,
                                                     String attributeName,
                                                     MethodParameter parameter,
                                                     WebDataBinderFactory binderFactory,
                                                     NativeWebRequest request) throws Exception {
        DataBinder binder = binderFactory.createBinder(request, null, attributeName);
        ConversionService conversionService = binder.getConversionService();
        if (conversionService != null) {
            TypeDescriptor source = TypeDescriptor.valueOf(String.class);
            TypeDescriptor target = new TypeDescriptor(parameter);
            if (conversionService.canConvert(source, target)) {
                return binder.convertIfNecessary(sourceValue, parameter.getParameterType(), parameter);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    protected void bindRequestParameters(ModelAndViewContainer mavContainer, WebDataBinderFactory binderFactory, WebDataBinder binder, NativeWebRequest request, MethodParameter parameter) throws Exception {
        ServletRequest servletRequest = prepareServletRequest(binder.getTarget(), request, parameter);
        List<Object> target = (List<Object>) binder.getTarget();
        for (String paramName : servletRequest.getParameterMap().keySet()) {
            String[] values = request.getParameterValues(paramName);
            if (values.length == 0 || StringUtil.isBlank(values[0])) {
                target.add(new PropertyFilter(paramName));
            } else if (values.length == 1) {
                target.add(new PropertyFilter(paramName, values[0]));
            } else {
                target.add(new PropertyFilter(paramName, values));
            }
        }
    }

    private ServletRequest prepareServletRequest(Object target, NativeWebRequest request, MethodParameter parameter) {
        String[] modelPrefixNames = this.modelPrefixNames;
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
        return mockRequest;
    }

    private boolean isPropertyFilterModelAttribute(String parameterName, String[] modelPrefixNames) {
        for (String modelPrefixName : modelPrefixNames) {
            if (parameterName.length() == modelPrefixName.length() + 1) {
                return false;
            }
            if (parameterName.startsWith(modelPrefixName + "_")) {
                return true;
            }
        }
        return false;
    }

    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
            }
        }
    }

    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter parameter) {
        int i = parameter.getParameterIndex();
        Class<?>[] paramTypes = parameter.getMethod().getParameterTypes();
        boolean hasBindingResult = paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]);

        return !hasBindingResult;
    }

}
