package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexEmbedBy;
import com.fantasy.framework.lucene.cache.PropertysCache;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;

public class EmbedFieldHandler extends AbstractFieldHandler {

    public EmbedFieldHandler(Object obj, Property property, String prefix) {
        super(obj, property, prefix);
    }

    public void handle(Document doc) {
        Object embedObj = this.property.getValue(this.obj);
        if (embedObj == null) {
            return;
        }
        Class<?> clazz = ClassUtil.getRealType(this.property);
        for (Property p : PropertysCache.getInstance().filter(clazz, IndexEmbedBy.class)) {
            FieldHandler handler = new EmbedByFieldHandler(this.obj.getClass(), embedObj, p, this.prefix);
            handler.handle(doc);
        }
    }
}
