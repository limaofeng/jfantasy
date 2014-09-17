package com.fantasy.framework.lucene.handler;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.lucene.document.Document;

import com.fantasy.framework.lucene.annotations.IndexRefBy;

public class RefByFieldHandler extends ByFieldHandler {
	private Class<?> refBy;

	public RefByFieldHandler(Class<?> refBy, Object obj, Field field, String prefix) {
		super(obj, field, prefix);
		this.refBy = refBy;
	}

	public void handle(Document doc) {
		IndexRefBy irb = (IndexRefBy) this.field.getAnnotation(IndexRefBy.class);
		Class<?>[] cls = irb.value();
		int len = cls.length;
		for (int i = 0; i < len; i++)
			if (cls[i].equals(this.refBy)) {
				boolean analyze = false;
				boolean[] as = irb.analyze();
				if (as.length > 0) {
					analyze = as[i];
				}
				boolean store = false;
				boolean[] ss = irb.store();
				if (ss.length > 0) {
					store = ss[i];
				}
				float boost = 1.0F;
				float[] bs = irb.boost();
				if (bs.length > 0) {
					boost = bs[i];
				}
				if ((this.obj instanceof List<?>)) {
					processList((List<?>) this.obj, doc, analyze, store, boost);
					break;
				}
				process(doc, analyze, store, boost);
				break;
			}
	}
}