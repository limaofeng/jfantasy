package com.fantasy.framework.lucene.handler;

import java.lang.reflect.Field;

import org.apache.lucene.document.Document;

import com.fantasy.framework.lucene.annotations.IndexRefBy;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.exception.IdException;
import com.fantasy.framework.lucene.mapper.FieldUtil;

public class RefFieldHandler extends AbstractFieldHandler {

	public RefFieldHandler(Object obj, Field field, String prefix) {
		super(obj, field, prefix);
	}

	public void handle(Document doc) {
		Object entity = FieldUtil.get(this.obj, this.field);
		if (entity == null) {
			return;
		}
		Class<?> clazz = FieldUtil.getRealType(this.field);
		if (entity != null) {
			Field[] fields = FieldsCache.getInstance().get(clazz);
			for (Field f : fields) {
				IndexRefBy irb = (IndexRefBy) f.getAnnotation(IndexRefBy.class);
				if (irb != null) {
					FieldHandler handler = new RefByFieldHandler(this.obj.getClass(), entity, f, this.prefix);
					handler.handle(doc);
				}
			}
		}
	}

	@SuppressWarnings("unused")
	private String getEntityId(Object obj) {
		try {
			Field field = FieldsCache.getInstance().getIdField(obj.getClass());
			return String.valueOf(FieldUtil.get(obj, field));
		} catch (IdException e) {
			throw new RuntimeException("要索引的对象@Id字段不能为空");
		}
	}
}