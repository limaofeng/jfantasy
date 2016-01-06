package org.jfantasy.framework.log.annotation;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.util.Assert;

@SuppressWarnings("serial")
public class AnnotationLogOperationSource extends AbstractFallbackLogOperationSource implements Serializable {

    private final boolean publicMethodsOnly;

    private final Set<LogAnnotationParser> annotationParsers;

    public AnnotationLogOperationSource() {
        this(true);
    }

    public AnnotationLogOperationSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
        this.annotationParsers = new LinkedHashSet<LogAnnotationParser>(1);
        this.annotationParsers.add(new SpringLogAnnotationParser());
    }


    public AnnotationLogOperationSource(LogAnnotationParser... annotationParsers) {
        this.publicMethodsOnly = true;
        Assert.notEmpty(annotationParsers, "At least one LogAnnotationParser needs to be specified");
        Set<LogAnnotationParser> parsers = new LinkedHashSet<LogAnnotationParser>(annotationParsers.length);
        Collections.addAll(parsers, annotationParsers);
        this.annotationParsers = parsers;
    }

    @Override
    protected Collection<LogOperation> findOperations(Class<?> clazz) {
        return determineLogOperations(clazz);
    }

    @Override
    protected Collection<LogOperation> findOperations(Method method) {
        return determineLogOperations(method);
    }

    protected Collection<LogOperation> determineLogOperations(AnnotatedElement ae) {
        Collection<LogOperation> ops = null;
        for (LogAnnotationParser annotationParser : this.annotationParsers) {
            Collection<LogOperation> annOps = annotationParser.parseLogAnnotations(ae);
            if (annOps != null) {
                if (ops == null) {
                    ops = new ArrayList<LogOperation>();
                }
                ops.addAll(annOps);
            }
        }
        return ops;
    }

    @Override
    protected boolean allowPublicMethodsOnly() {
        return this.publicMethodsOnly;
    }
}
