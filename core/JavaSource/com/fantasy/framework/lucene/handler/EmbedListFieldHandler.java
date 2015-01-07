package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexEmbedBy;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import org.apache.lucene.document.Document;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmbedListFieldHandler extends AbstractFieldHandler {
	public EmbedListFieldHandler(Object obj, Field field, String prefix) {
		super(obj, field, prefix);
	}

	@SuppressWarnings("unchecked")
	public void handle(Document doc) {
		Object value = FieldUtil.get(this.obj, this.field);
		if (value == null) {
			return;
		}
		List<Object> list = null;
		Class<?> clazz;
		Class<?> type = this.field.getType();
		if (type.isArray()) {
			clazz = type.getComponentType();
			int len = Array.getLength(value);
			list = new ArrayList<Object>();
			for (int i = 0; i < len; i++){
                list.add(Array.get(value, i));
            }
		} else {
			ParameterizedType paramType = (ParameterizedType) this.field.getGenericType();
			Type[] types = paramType.getActualTypeArguments();
			if (types.length == 1) {
				clazz = (Class<?>) types[0];
				if (DataType.isList(type)) {
					list = (List<Object>) value;
				} else if (DataType.isSet(type)) {
					Set<?> set = (Set<?>) value;
					list = new ArrayList<Object>();
					list.addAll(set);
				}
			} else if (types.length == 2) {
				clazz = (Class<?>) types[1];
				Map<?, ?> map = (Map<?, ?>) value;
				list = new ArrayList<Object>();
				list.addAll(map.values());
			} else {
				return;
			}
		}
		if ((list != null) && (list.size() > 0)) {
			Field[] fields = FieldsCache.getInstance().get(clazz);
			for (Field f : fields) {
				IndexEmbedBy ieb = (IndexEmbedBy) f.getAnnotation(IndexEmbedBy.class);
				if (ieb != null) {
					FieldHandler handler = new EmbedByFieldHandler(this.obj.getClass(), list, f, this.prefix);
					handler.handle(doc);
				}
			}
		}
	}
}
