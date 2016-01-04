package org.jfantasy.framework.lucene.handler;

import org.jfantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

public class IdFieldHandler extends AbstractFieldHandler {

    public IdFieldHandler(Object obj, Property property, String prefix) {
        super(obj, property, prefix);
    }

    public void handle(Document doc) {
        String fieldName = this.property.getName();
        doc.add(new Field(fieldName, getEntityId(), Field.Store.YES, Field.Index.NOT_ANALYZED));
    }

    private String getEntityId() {
        Object id = this.property.getValue(this.obj);
        if (id == null) {
            throw new RuntimeException("要索引的对象@Id字段不能为空");
        }
        return String.valueOf(id);
    }

}
