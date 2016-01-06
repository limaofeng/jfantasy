package org.jfantasy.framework.lucene.handler;

import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.lucene.annotations.IndexRefBy;
import org.jfantasy.framework.lucene.cache.PropertysCache;
import org.jfantasy.framework.lucene.exception.IdException;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.reflect.Property;
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