/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jfantasy.framework.spring.mvc.method.annotation;

import org.jfantasy.framework.spring.mvc.bind.annotation.RequestJsonParam;
import org.jfantasy.framework.spring.mvc.util.MapWapper;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.AbstractNamedValueMethodArgumentResolver;

import javax.servlet.ServletException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * 解析请求参数json字符串
 *
 * @author Zhang Kaitao
 * @since 3.1
 */
public class RequestJsonParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver implements WebArgumentResolver {

    private ObjectMapper mapper = new ObjectMapper();

    public RequestJsonParamMethodArgumentResolver() {
        super(null);
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestJsonParam.class);
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestJsonParam annotation = parameter.getParameterAnnotation(RequestJsonParam.class);
        return new RequestJsonParamNamedValueInfo(annotation);

    }

    @Override
    @SuppressWarnings("unchecked")
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest webRequest) throws Exception {
        RequestJsonParam annotation = parameter.getParameterAnnotation(RequestJsonParam.class);
        String[] paramValues = webRequest.getParameterValues(name);
        Class<?> paramType = parameter.getParameterType();
        if (paramValues == null) {
            return null;

        }

        try {
            if (paramValues.length == 1) {
                String text = paramValues[0];
                Type type = parameter.getGenericParameterType();

                if (MapWapper.class.isAssignableFrom(paramType)) {
                    MapWapper<?, ?> jsonMap = (MapWapper<?, ?>) paramType.newInstance();

                    MapType mapType = (MapType) getJavaType(HashMap.class);

                    if (type instanceof ParameterizedType) {
                        mapType = mapType.withKeyType((JavaType) ((ParameterizedType) type).getActualTypeArguments()[0]);
                        mapType = (MapType) mapType.withContentType((JavaType) ((ParameterizedType) type).getActualTypeArguments()[1]);
                    }
                    jsonMap.setInnerMap(mapper.<Map>readValue(text, mapType));
                    return jsonMap;
                }

                JavaType javaType = getJavaType(paramType);


                if (Collection.class.isAssignableFrom(paramType)) {
                    javaType = javaType.withContentType((JavaType) ((ParameterizedType) type).getActualTypeArguments()[0]);
                }

                return mapper.readValue(paramValues[0], javaType);
            }

        } catch (Exception e) {
            throw new JsonMappingException("Could not read request json parameter", e);
        }

        throw new UnsupportedOperationException(
                "too many request json parameter '" + name + "' for method parameter type [" + paramType + "], only support one json parameter");
    }

    protected JavaType getJavaType(Class<?> clazz) {
        return TypeFactory.defaultInstance().constructArrayType(clazz);
    }

    @Override
    protected void handleMissingValue(String paramName, MethodParameter parameter) throws ServletException {
        String paramType = parameter.getParameterType().getName();
        throw new ServletRequestBindingException(
                "Missing request json parameter '" + paramName + "' for method parameter type [" + paramType + "]");
    }


    private class RequestJsonParamNamedValueInfo extends NamedValueInfo {

        private RequestJsonParamNamedValueInfo() {
            super("", false, null);
        }

        private RequestJsonParamNamedValueInfo(RequestJsonParam annotation) {
            super(annotation.value(), annotation.required(), null);
        }
    }

    /**
     * spring 3.1之前
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, NativeWebRequest request) throws Exception {
        if (!supportsParameter(parameter)) {
            return WebArgumentResolver.UNRESOLVED;
        }
        return resolveArgument(parameter, null, request, null);
    }
}