package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexRefBy;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RefListFieldHandler extends AbstractFieldHandler {

    public RefListFieldHandler(Object obj, Property property, String prefix) {
        super(obj, property, prefix);
    }

    public void handle(Document doc) {
        Object value = this.property.getValue(this.obj);
        if (value == null) {
            return;
        }
        Class<?> clazz;
        Class<?> type = this.property.getPropertyType();
        List<Object> list = new ArrayList<Object>();
        if (type.isArray()) {
            clazz = type.getComponentType();
        } else {
            ParameterizedType paramType = this.property.getGenericType();
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
        clazz = ClassUtil.getRealType(clazz);
        if (!list.isEmpty()) {
            for (Property p : PropertysCache.getInstance().filter(clazz, IndexRefBy.class)) {
                FieldHandler handler = new RefByFieldHandler(this.obj.getClass(), list, p, this.prefix);
                handler.handle(doc);
            }
        }
    }
}