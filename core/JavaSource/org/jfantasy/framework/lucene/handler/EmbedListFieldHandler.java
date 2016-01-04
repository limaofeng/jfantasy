package org.jfantasy.framework.lucene.handler;

import org.jfantasy.framework.lucene.annotations.IndexEmbedBy;
import org.jfantasy.framework.lucene.cache.PropertysCache;
import org.jfantasy.framework.lucene.mapper.DataType;
import org.jfantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;

import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EmbedListFieldHandler extends AbstractFieldHandler {
    public EmbedListFieldHandler(Object obj, Property property, String prefix) {
        super(obj, property, prefix);
    }

    @SuppressWarnings("unchecked")
    public void handle(Document doc) {
        Object value = this.property.getValue(this.obj);
        if (value == null) {
            return;
        }
        List<Object> list = null;
        Class<?> clazz;
        Class<?> type = this.property.getPropertyType();
        if (type.isArray()) {
            clazz = type.getComponentType();
            int len = Array.getLength(value);
            list = new ArrayList<Object>();
            for (int i = 0; i < len; i++) {
                list.add(Array.get(value, i));
            }
        } else {
            ParameterizedType paramType = this.property.getGenericType();
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
        if ((list != null) && (!list.isEmpty())) {
            for (Property p : PropertysCache.getInstance().filter(clazz, IndexEmbedBy.class)) {
                FieldHandler handler = new EmbedByFieldHandler(this.obj.getClass(), list, p, this.prefix);
                handler.handle(doc);
            }
        }
    }
}
