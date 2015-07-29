package com.fantasy.framework.log.annotation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.util.Assert;

@SuppressWarnings("serial")
public class CompositeLogOperationSource implements LogOperationSource, Serializable {

    private final LogOperationSource[] logOperationSources;

    public CompositeLogOperationSource(LogOperationSource... logOperationSources) {
        Assert.notEmpty(logOperationSources, "logOperationSources array must not be empty");
        this.logOperationSources = logOperationSources;
    }

    public final LogOperationSource[] getLogOperationSources() {
        return this.logOperationSources;
    }

    public Collection<LogOperation> getOperations(Method method, Class<?> targetClass) {
        Collection<LogOperation> ops = null;
        for (LogOperationSource source : this.logOperationSources) {
            Collection<LogOperation> logOperations = source.getOperations(method, targetClass);
            if (logOperations != null) {
                if (ops == null) {
                    ops = new ArrayList<LogOperation>();
                }
                ops.addAll(logOperations);
            }
        }
        return ops;
    }
}
