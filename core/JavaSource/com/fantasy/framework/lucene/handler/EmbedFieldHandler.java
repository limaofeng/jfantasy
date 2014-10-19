package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexEmbedBy;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import org.apache.lucene.document.Document;

import java.lang.reflect.Field;

public class EmbedFieldHandler extends AbstractFieldHandler {
	public EmbedFieldHandler(Object obj, Field field, String prefix) {
		super(obj, field, prefix);
	}

	public void handle(Document doc) {
		Object embedObj = FieldUtil.get(this.obj, this.field);
		if (embedObj == null) {
			return;
		}
		Class<?> clazz = FieldUtil.getRealType(this.field);
		Field[] fields = FieldsCache.getInstance().get(clazz);
		for (Field f : fields) {
			IndexEmbedBy ieb = f.getAnnotation(IndexEmbedBy.class);
			if (ieb != null) {
				FieldHandler handler = new EmbedByFieldHandler(this.obj.getClass(), embedObj, f, this.prefix);
				handler.handle(doc);
			}
		}
	}
}
