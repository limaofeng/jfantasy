package com.fantasy.framework.log.annotation;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

public interface LogAnnotationParser {

    Collection<LogOperation> parseLogAnnotations(AnnotatedElement ae);

}
