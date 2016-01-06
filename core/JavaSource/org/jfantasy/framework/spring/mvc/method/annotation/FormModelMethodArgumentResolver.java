package org.jfantasy.framework.spring.mvc.method.annotation;

import org.jfantasy.framework.spring.mvc.bind.annotation.FormModel;
import org.jfantasy.framework.spring.mvc.util.MapWapper;
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
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
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
import java.util.Map.Entry;


public class FormModelMethodArgumentResolver implements HandlerMethodArgumentResolver {

    public FormModelMethodArgumentResolver() {
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(FormModel.class);
    }

    public final Object resolveArgument(MethodParameter parameter,
                                        ModelAndViewContainer mavContainer,
                                        NativeWebRequest request,
                                        WebDataBinderFactory binderFactory) throws Exception {
        String name = parameter.getParameterAnnotation(FormModel.class).value();

        Object target = (mavContainer.containsAttribute(name)) ?
                mavContainer.getModel().get(name) : createAttribute(name, parameter, binderFactory, request);

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

    protected Object createAttribute(String attributeName, MethodParameter parameter,
                                     WebDataBinderFactory binderFactory, NativeWebRequest request) throws Exception {

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
        if (Set.class.isAssignableFrom(parameterType)) {
            return HashSet.class.newInstance();
        }

        if (MapWapper.class.isAssignableFrom(parameterType)) {
            return MapWapper.class.newInstance();
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
    protected void bindRequestParameters(
            ModelAndViewContainer mavContainer,
            WebDataBinderFactory binderFactory,
            WebDataBinder binder,
            NativeWebRequest request,
            MethodParameter parameter) throws Exception {


        Class<?> targetType = binder.getTarget().getClass();
        ServletRequest servletRequest = prepareServletRequest(binder.getTarget(), request, parameter);
        WebDataBinder simpleBinder = binderFactory.createBinder(request, null, null);

        if (Collection.class.isAssignableFrom(targetType)) {//bind collection

            Type type = parameter.getGenericParameterType();
            Class<?> componentType = Object.class;

            Collection target = (Collection) binder.getTarget();

            if (type instanceof ParameterizedType) {
                componentType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
            }

            if (parameter.getParameterType().isArray()) {
                componentType = parameter.getParameterType().getComponentType();
            }


            for (Object key : servletRequest.getParameterMap().keySet()) {
                String prefixName = getPrefixName((String) key);
                if (isSimpleComponent(prefixName)) { //bind simple type
                    Map<String, Object> paramValues = WebUtils.getParametersStartingWith(servletRequest, prefixName);
                    for (Object value : paramValues.values()) {
                        target.add(simpleBinder.convertIfNecessary(value, componentType));
                    }
                } else {

                    Object component = BeanUtils.instantiate(componentType);
                    WebDataBinder componentBinder = binderFactory.createBinder(request, component, null);
                    component = componentBinder.getTarget();

                    if (component != null) {
                        ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(servletRequest, prefixName, "");
                        componentBinder.bind(pvs);
                        validateIfApplicable(componentBinder, parameter);
                        if (componentBinder.getBindingResult().hasErrors()) {
                            if (isBindExceptionRequired(componentBinder, parameter)) {
                                throw new BindException(componentBinder.getBindingResult());
                            }
                        }

                        target.add(component);
                    }
                }
            }
        } else if (MapWapper.class.isAssignableFrom(targetType)) {


            Type type = parameter.getGenericParameterType();
            Class<?> keyType = Object.class;
            Class<?> valueType = Object.class;

            if (type instanceof ParameterizedType) {
                keyType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];
                valueType = (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[1];
            }


            Map target = new HashMap();
            ((MapWapper) binder.getTarget()).setInnerMap(target);

            for (Object key : servletRequest.getParameterMap().keySet()) {
                String prefixName = getPrefixName((String) key);

                Object keyValue = simpleBinder.convertIfNecessary(getMapKey(prefixName), keyType);

                if (isSimpleComponent(prefixName)) { //bind simple type
                    Map<String, Object> paramValues = WebUtils.getParametersStartingWith(servletRequest, prefixName);

                    for (Object value : paramValues.values()) {
                        target.put(keyValue, simpleBinder.convertIfNecessary(value, valueType));
                    }
                } else {
                    Object component = BeanUtils.instantiate(valueType);
                    WebDataBinder componentBinder = binderFactory.createBinder(request, component, null);
                    component = componentBinder.getTarget();

                    if (component != null) {
                        ServletRequestParameterPropertyValues pvs = new ServletRequestParameterPropertyValues(servletRequest, prefixName, "");
                        componentBinder.bind(pvs);

                        validateComponent(componentBinder, parameter);

                        target.put(keyValue, component);
                    }
                }
            }
        } else {//bind model
            ServletRequestDataBinder servletBinder = (ServletRequestDataBinder) binder;
            servletBinder.bind(servletRequest);
        }
    }


    private Object getMapKey(String prefixName) {
        String key = prefixName;
        if (key.startsWith("['")) {
            key = key.replaceAll("\\[\'", "").replaceAll("\'\\]", "");
        }
        if (key.startsWith("[\"")) {
            key = key.replaceAll("\\[\"", "").replaceAll("\"\\]", "");
        }
        if (key.endsWith(".")) {
            key = key.substring(0, key.length() - 1);
        }
        return key;
    }

    private boolean isSimpleComponent(String prefixName) {
        return !prefixName.endsWith(".");
    }

    private String getPrefixName(String name) {

        int begin = 0;

        int end = name.indexOf("]") + 1;

        if (name.contains("].")) {
            end = end + 1;
        }

        return name.substring(begin, end);
    }

    @SuppressWarnings("unchecked")
    private ServletRequest prepareServletRequest(Object target, NativeWebRequest request, MethodParameter parameter) {

        String modelPrefixName = parameter.getParameterAnnotation(FormModel.class).value();

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
        for (Entry<String, String> entry : getUriTemplateVariables(request).entrySet()) {
            String parameterName = entry.getKey();
            String value = entry.getValue();
            if (isFormModelAttribute(parameterName, modelPrefixName)) {
                mockRequest.setParameter(getNewParameterName(parameterName, modelPrefixName), value);
            }
        }

        for (Object parameterEntry : nativeRequest.getParameterMap().entrySet()) {
            Entry<String, String[]> entry = (Entry<String, String[]>) parameterEntry;
            String parameterName = entry.getKey();
            String[] value = entry.getValue();
            if (isFormModelAttribute(parameterName, modelPrefixName)) {
                mockRequest.setParameter(getNewParameterName(parameterName, modelPrefixName), value);
            }
        }


        return mockRequest;
    }

    private String getNewParameterName(String parameterName, String modelPrefixName) {
        int modelPrefixNameLength = modelPrefixName.length();

        if (parameterName.charAt(modelPrefixNameLength) == '.') {
            return parameterName.substring(modelPrefixNameLength + 1);
        }

        if (parameterName.charAt(modelPrefixNameLength) == '[') {
            return parameterName.substring(modelPrefixNameLength);
        }
        throw new IllegalArgumentException("illegal request parameter, can not binding to @FormBean(" + modelPrefixName + ")");
    }

    private boolean isFormModelAttribute(String parameterName, String modelPrefixName) {
        int modelPrefixNameLength = modelPrefixName.length();

        if (parameterName.length() == modelPrefixNameLength) {
            return false;
        }

        if (!parameterName.startsWith(modelPrefixName)) {
            return false;
        }


        char ch = parameterName.charAt(modelPrefixNameLength);

        return ch == '.' || ch == '[';

    }

    protected void validateComponent(WebDataBinder binder, MethodParameter parameter) throws BindException {

        boolean validateParameter = validateParameter(parameter);
        Annotation[] annotations = binder.getTarget().getClass().getAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid") && validateParameter) {
                Object hints = AnnotationUtils.getValue(annot);
                binder.validate(hints instanceof Object[] ? (Object[]) hints : new Object[]{hints});
            }
        }

        if (binder.getBindingResult().hasErrors()) {
            if (isBindExceptionRequired(binder, parameter)) {
                throw new BindException(binder.getBindingResult());
            }
        }
    }

    private boolean validateParameter(MethodParameter parameter) {
        Annotation[] annotations = parameter.getParameterAnnotations();
        for (Annotation annot : annotations) {
            if (annot.annotationType().getSimpleName().startsWith("Valid")) {
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
