package com.fantasy.framework.log.annotation;

import java.lang.reflect.Method;
import java.util.Collection;

public interface LogOperationSource {

    Collection<LogOperation> getOperations(Method method, Class<?> targetClass);

}
