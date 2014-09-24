package com.fantasy.framework.util.reflect;

import com.fantasy.framework.util.common.JavassistUtil;
import net.sf.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodProxy {
    private Object method;
    private Class<?>[] parameterTypes;
    private Class<?> returnType;

    public MethodProxy(Object method) {
        this.method = method;
        if ((method instanceof FastMethod)) {
            this.parameterTypes = ((FastMethod) method).getParameterTypes();
            this.returnType = ((FastMethod) method).getReturnType();
        } else {
            this.parameterTypes = ((Method) method).getParameterTypes();
            this.returnType = ((Method) method).getReturnType();
        }
    }

    public MethodProxy(Object method, Class<?>[] parameterTypes) {
        this(method);
        this.parameterTypes = parameterTypes;
    }

    public MethodProxy(Object method, Class<?> parameterType) {
        this(method);
        if (parameterType != null)
            this.parameterTypes = new Class[]{parameterType};
    }

    public Object invoke(Object object, Object param) {
        return invoke(object, new Object[]{param});
    }

    public Object invoke(Object object, Object... params) {
        try {
            if ((this.method instanceof FastMethod)) {
                if (params.length > 0) {
                    return ((FastMethod) this.method).invoke(object, params);
                }
                return ((FastMethod) this.method).invoke(object, params);
            }

            if (params.length > 0) {
                return ((Method) this.method).invoke(object, params);
            }
            return ((Method) this.method).invoke(object, params);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MethodProxy create(Object method) {
        if (method == null)
            return null;
        return new MethodProxy(method);
    }

    public Class<?>[] getParameterTypes() {
        return this.parameterTypes;
    }

    public Class<?> getReturnType() {
        return this.returnType;
    }

    public String toString() {
        return "MethodProxy [" + this.method.toString() + "]";
    }

    public Method getMethod() {
        return (this.method instanceof FastMethod) ? ((FastMethod) this.method).getJavaMethod() : (Method) this.method;
    }

    public String[] getParamNames() {
        try {
            if ((this.method instanceof FastMethod)) {
                Class<?> declaringClass = ((FastMethod) this.method).getDeclaringClass();
                FastMethod method = (FastMethod) this.method;
                return JavassistUtil.getParamNames(declaringClass.getName(), method.getName(), this.parameterTypes);
            }
            Class<?> declaringClass = ((Method) this.method).getDeclaringClass();
            Method method = (Method) this.method;
            return JavassistUtil.getParamNames(declaringClass.getName(), method.getName(), this.parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}