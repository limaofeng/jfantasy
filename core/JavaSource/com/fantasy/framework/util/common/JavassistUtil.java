package com.fantasy.framework.util.common;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;

public class JavassistUtil {

    private final static ConcurrentHashMap<String, CtClass> ctClassCache = new ConcurrentHashMap<String, CtClass>();

    public static ClassPool getDefault() throws NotFoundException {
        ClassPool classPool = ClassPool.getDefault();
        classPool.appendClassPath(PathUtil.root());
        return classPool;
    }

    public static CtClass getCtClass(String classname) throws NotFoundException {
        if (!ctClassCache.containsKey(classname)) {
            try {
                getDefault().insertClassPath(new ClassClassPath(Class.forName(classname)));
            } catch (ClassNotFoundException e) {
                throw new NotFoundException(e.getMessage(), e);
            }
            ctClassCache.putIfAbsent(classname, getDefault().get(classname));
        }
        return ctClassCache.get(classname);
    }

    public static String[] getParamNames(String classname, Method method) throws NotFoundException, MissingLVException {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] paramTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            paramTypeNames[i] = parameterTypes[i].getName();
        }
        CtMethod cm = getCtClass(classname).getDeclaredMethod(method.getName(), getDefault().get(paramTypeNames));
        return getParamNames(cm);
    }

    /**
     * 获取方法参数名称
     *
     * @param classname
     * @param methodname
     * @param parameterTypes
     * @return
     * @throws NotFoundException
     * @throws MissingLVException
     */
    public static String[] getParamNames(String classname, String methodname, Class<?>... parameterTypes) throws NotFoundException, MissingLVException {
        String[] paramTypeNames = new String[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            paramTypeNames[i] = parameterTypes[i].getName();
        }
        CtMethod cm = getCtClass(classname).getDeclaredMethod(methodname, getDefault().get(paramTypeNames));
        return getParamNames(cm);
    }

    /**
     * 获取方法参数名称
     *
     * @param cm
     * @return
     * @throws NotFoundException
     * @throws MissingLVException 如果最终编译的class文件不包含局部变量表信息
     */
    protected static String[] getParamNames(CtMethod cm) throws NotFoundException, MissingLVException {
        CtClass cc = cm.getDeclaringClass();
        MethodInfo methodInfo = cm.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            throw new MissingLVException(cc.getName());
        }
        String[] paramNames = new String[cm.getParameterTypes().length];
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramNames.length; i++) {
            paramNames[i] = attr.variableName(i + pos);
        }
        return paramNames;
    }

    /**
     * 在class中未找到局部变量表信息<br>
     * 使用编译器选项 javac -g:{vars}来编译源文件
     *
     * @author Administrator
     */
    @SuppressWarnings("serial")
    public static class MissingLVException extends Exception {
        static String msg = "class:%s 不包含局部变量表信息，请使用编译器选项 javac -g:{vars}来编译源文件。";

        public MissingLVException(String clazzName) {
            super(String.format(msg, clazzName));
        }
    }

}