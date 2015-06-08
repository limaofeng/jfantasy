package com.fantasy.framework.struts2;

import java.util.Map;

/**
 * 用于动态bean不传递版本号时的Model生产
 */
public interface DynaModelDriven<T> {

    public T getModel(String methodName,String paramName,Class paramType,Map<String, Object> parameters);

}
