package org.jfantasy.framework.util.cglib;

import org.jfantasy.framework.util.cglib.interceptor.ValidatorInterceptor;

import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

public class CglibUtil {
    private CglibUtil() {
    }

    private final static ConcurrentHashMap<String, MethodInterceptor> defaultInterceptors = new ConcurrentHashMap<String, MethodInterceptor>();

    static {
        defaultInterceptors.put("validator", new ValidatorInterceptor());
    }

    private final static ConcurrentHashMap<Class<?>, Enhancer> enhancerCache = new ConcurrentHashMap<Class<?>, Enhancer>();

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<T> classType, MethodInterceptor interceptor) {
        if (!enhancerCache.containsKey(classType)) {
            enhancerCache.putIfAbsent(classType, newEnhancer(classType, interceptor));
        }
        return (T) enhancerCache.get(classType).create();
    }

    private static <T> Enhancer newEnhancer(Class<T> classType, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(classType);
        enhancer.setCallback(interceptor);
        return enhancer;
    }

    public static MethodInterceptor getDefaultInterceptor(String key) {
        return defaultInterceptors.get(key);
    }
}