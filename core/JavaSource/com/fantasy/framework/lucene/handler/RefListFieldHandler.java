package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexRefBy;
import com.fantasy.framework.lucene.cache.FieldsCache;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import org.apache.lucene.document.Document;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RefListFieldHandler extends AbstractFieldHandler {
    public RefListFieldHandler(Object obj, Field field, String prefix) {
        super(obj, field, prefix);
    }

    public void handle(Document doc) {
        Object value = FieldUtil.get(this.obj, this.field);
        if (value == null) {
            return;
        }
        Class<?> clazz;
        Class<?> type = this.field.getType();
        List<Object> list = new ArrayList<Object>();
        if (type.isArray()) {
            clazz = type.getComponentType();
        } else {
            ParameterizedType paramType = (ParameterizedType) this.field.getGenericType();
            Type[] types = paramType.getActualTypeArguments();
            if (types.length == 1) {
                clazz = (Class<?>) types[0];
                if (DataType.isList(type)) {
                    List<?> li = (List<?>) value;
                    for (Object ent : li) {
                        if (ent != null) {
                            list.add(ent);
                        }
                    }
                } else if (DataType.isSet(type)) {
                    Set<?> set = (Set<?>) value;
                    for (Object ent : set) {
                        if (ent != null) {
                            list.add(ent);
                        }
                    }
                }
            } else if (types.length == 2) {
                clazz = (Class<?>) types[1];
                Map<?, ?> map = (Map<?, ?>) value;
                for (Map.Entry<?, ?> entry : map.entrySet()) {
                    Object ent = entry.getValue();
                    if (ent != null) {
                        list.add(ent);
                    }
                }
            } else {
                return;
            }
        }
        clazz = FieldUtil.getRealType(clazz);
        if (!list.isEmpty()) {
            Field[] fields = FieldsCache.getInstance().get(clazz);
            for (Field f : fields) {
                IndexRefBy irb = (IndexRefBy) f.getAnnotation(IndexRefBy.class);
                if (irb != null) {
                    FieldHandler handler = new RefByFieldHandler(this.obj.getClass(), list, f, this.prefix);
                    handler.handle(doc);
                }
            }
        }
    }
}