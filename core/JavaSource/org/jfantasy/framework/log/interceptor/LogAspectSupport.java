package org.jfantasy.framework.log.interceptor;

import org.jfantasy.framework.log.LogManager;
import org.jfantasy.framework.log.annotation.CompositeLogOperationSource;
import org.jfantasy.framework.log.annotation.LogOperation;
import org.jfantasy.framework.log.annotation.LogOperationSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.interceptor.DefaultKeyGenerator;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.expression.EvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

public class LogAspectSupport implements InitializingBean {

    public interface Invoker {
        Object invoke();
    }

    private final ExpressionEvaluator evaluator = new ExpressionEvaluator();

    private LogOperationSource logOperationSource;

    protected final Log LOGGER = LogFactory.getLog(getClass());

    private LogManager logManager;

    private KeyGenerator keyGenerator = new DefaultKeyGenerator();

    private boolean initialized = false;

    public void setLogManager(LogManager logManager) {
        this.logManager = logManager;
    }

    public LogManager getLogManager() {
        return this.logManager;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public KeyGenerator getKeyGenerator() {
        return this.keyGenerator;
    }

    public void afterPropertiesSet() {

        this.initialized = true;
    }

    public void setLogOperationSources(LogOperationSource... logOperationSources) {
        Assert.notEmpty(logOperationSources);
        this.logOperationSource = (logOperationSources.length > 1 ? new CompositeLogOperationSource(logOperationSources) : logOperationSources[0]);
    }

    protected String methodIdentification(Method method, Class<?> targetClass) {
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        return ClassUtils.getQualifiedMethodName(specificMethod);
    }

    public LogOperationSource getLogOperationSource() {
        return this.logOperationSource;
    }

    protected Object execute(Invoker invoker, Object target, Method method, Object[] args) {
        if (!this.initialized) {
            return invoker.invoke();
        }
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
        if (targetClass == null) {
            targetClass = target.getClass();
        }
        final Collection<LogOperation> logOp = getLogOperationSource().getOperations(method, targetClass);
        if (!CollectionUtils.isEmpty(logOp)) {
            Collection<LogOperationContext> ops = createOperationContext(logOp, method, args, target, targetClass);
            for (LogOperationContext context : ops) {
                LOGGER.debug("Log Info : " + context.isConditionPassing() + " - " + context.text());
            }
        }
        return invoker.invoke();
    }

    private Collection<LogOperationContext> createOperationContext(Collection<LogOperation> logOp, Method method, Object[] args, Object target, Class<?> targetClass) {
        Collection<LogOperationContext> logables = new ArrayList<LogOperationContext>();

        for (LogOperation logOperation : logOp) {
            LogOperationContext opContext = getOperationContext(logOperation, method, args, target, targetClass);
            logables.add(opContext);
        }

        return logables;
    }

    protected LogOperationContext getOperationContext(LogOperation operation, Method method, Object[] args, Object target, Class<?> targetClass) {
        return new LogOperationContext(operation, method, args, target, targetClass);
    }

    protected class LogOperationContext {

        private final LogOperation operation;

        private final Object target;

        private final Method method;

        private final Object[] args;

        private final EvaluationContext evalContext;

        public LogOperationContext(LogOperation operation, Method method, Object[] args, Object target, Class<?> targetClass) {
            this.operation = operation;
            this.target = target;
            this.method = method;
            this.args = args;

            this.evalContext = evaluator.createEvaluationContext(method, args, target, targetClass);
        }

        protected boolean isConditionPassing() {
            return !StringUtils.hasText(this.operation.getCondition()) || evaluator.condition(this.operation.getCondition(), this.method, this.evalContext);
        }

        protected Object text() {
            if (StringUtils.hasText(this.operation.getText())) {
                return evaluator.key(this.operation.getText(), this.method, this.evalContext);
            }
            return keyGenerator.generate(this.target, this.method, this.args);
        }
    }

}
