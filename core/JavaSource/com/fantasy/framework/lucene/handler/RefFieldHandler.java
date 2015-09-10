package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.error.IgnoreException;
import com.fantasy.framework.lucene.annotations.IndexRefBy;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.lucene.exception.IdException;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;

public class RefFieldHandler extends AbstractFieldHandler {

    public RefFieldHandler(Object obj, Property property, String prefix) {
        super(obj, property, prefix);
    }

    public void handle(Document doc) {
        Object entity = this.property.getValue(this.obj);
        if (entity == null) {
            return;
        }
        Class<?> clazz = ClassUtil.getRealType(this.property);
        Property[] properties = PropertysCache.getInstance().get(clazz);
        for (Property p : properties) {
            IndexRefBy irb = p.getAnnotation(IndexRefBy.class);
            if (irb != null) {
                FieldHandler handler = new RefByFieldHandler(this.obj.getClass(), entity, p, this.prefix);
                handler.handle(doc);
            }
        }
    }

    private String getEntityId(Object obj) {
        try {
            Property property = PropertysCache.getInstance().getIdProperty(obj.getClass());
            return String.valueOf(property.getValue(obj));
        } catch (IdException e) {
            throw new IgnoreException("要索引的对象@Id字段不能为空", e);
        }
    }
}