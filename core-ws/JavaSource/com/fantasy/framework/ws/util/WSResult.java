package com.fantasy.framework.ws.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * WebService返回结果基类,定义所有返回码.
 */
public class WSResult {

    private final static Log logger = LogFactory.getLog(WSResult.class);

    //-- 返回代码定义 --//
    // 按项目的规则进行定义，比如1xx代表客户端参数错误，2xx代表业务错误等.
    public static final String SUCCESS = "0";
    public static final String PARAMETER_ERROR = "101";
    public static final String IMAGE_ERROR = "201";

    public static final String SYSTEM_ERROR = "500";
    public static final String SYSTEM_ERROR_MESSAGE = "Runtime unknown internal error.";

    //-- WSResult基本属性 --//
    private String code = SUCCESS;
    private String message;

    public static WSResult buildResult(String resultCode, String resultMessage) {
        WSResult result = new WSResult();
        result.setResult(resultCode, resultMessage);
        return result;
    }

    /**
     * 创建结果.
     */
    public static <T extends WSResult> T buildResult(Class<T> resultClass, String resultCode, String resultMessage) {
        try {
            T result = resultClass.newInstance();
            result.setResult(resultCode, resultMessage);
            return result;
        } catch (InstantiationException e) {
            logger.error(e);
            throw convertReflectionExceptionToUnchecked(e);
        } catch (IllegalAccessException e) {
            logger.error(e);
            throw convertReflectionExceptionToUnchecked(e);
        }
    }

    /**
     * 创建默认异常结果.
     */
    public static <T extends WSResult> T buildDefaultErrorResult(Class<T> resultClass) {
        return buildResult(resultClass, SYSTEM_ERROR, SYSTEM_ERROR_MESSAGE);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 设置返回结果.
     */
    public void setResult(String resultCode, String resultMessage) {
        this.code = resultCode;
        this.message = resultMessage;
    }

    private static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (((e instanceof IllegalAccessException)) || ((e instanceof IllegalArgumentException)) || ((e instanceof NoSuchMethodException))) {
            return new IllegalArgumentException("Reflection Exception.", e);
        }
        if ((e instanceof InvocationTargetException))
            return new RuntimeException("Reflection Exception.", ((InvocationTargetException) e).getTargetException());
        if ((e instanceof RuntimeException)) {
            return (RuntimeException) e;
        }
        return new RuntimeException("Unexpected Checked Exception.", e);
    }
}

