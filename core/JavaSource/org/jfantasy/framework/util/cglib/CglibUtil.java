package org.jfantasy.framework.util.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import java.util.concurrent.ConcurrentHashMap;

public class CglibUtil {
    private CglibUtil() {
    }

    private final static ConcurrentHashMap<String, MethodInterceptor> defaultInterceptors = new ConcurrentHashMap<String, MethodInterceptor>();

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