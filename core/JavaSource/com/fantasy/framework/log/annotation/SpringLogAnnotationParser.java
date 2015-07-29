package com.fantasy.framework.log.annotation;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;

@SuppressWarnings("serial")
public class SpringLogAnnotationParser implements LogAnnotationParser, Serializable {

	public Collection<LogOperation> parseLogAnnotations(AnnotatedElement ae) {
		Collection<LogOperation> ops = null;
		Collection<Log> logs = getAnnotations(ae, Log.class);
		if (logs != null) {
			ops = lazyInit(ops);
			for (Log LOG : logs) {
				ops.add(parseAnnotation(ae, LOG));
			}
		}
		return ops;
	}

	LogOperation parseAnnotation(AnnotatedElement ae, Log LOG) {
		LogOperation cuo = new LogOperation();
		cuo.setCondition(LOG.condition());
		cuo.setType(LOG.type());
		cuo.setText(LOG.text());
		return cuo;
	}

	private <T extends Annotation> Collection<LogOperation> lazyInit(Collection<LogOperation> ops) {
		return ops != null ? ops : new ArrayList<LogOperation>(1);
	}

	private static <T extends Annotation> Collection<T> getAnnotations(AnnotatedElement ae, Class<T> annotationType) {
		Collection<T> anns = new ArrayList<T>(2);
		T ann = ae.getAnnotation(annotationType);
		if (ann != null) {
			anns.add(ann);
		}
		for (Annotation metaAnn : ae.getAnnotations()) {
			ann = metaAnn.annotationType().getAnnotation(annotationType);
			if (ann != null) {
				anns.add(ann);
			}
		}
		return anns.isEmpty() ? null : anns;
	}

}
