package org.jfantasy.framework.lucene.handler;

import org.jfantasy.framework.util.reflect.Property;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.List;

public abstract class ByFieldHandler extends PropertyFieldHandler {

    protected ByFieldHandler(Object obj, Property property, String prefix) {
        super(obj, property, prefix);
    }

    protected void processList(List<?> objList, Document doc, boolean analyze, boolean store, float boost) {
        StringBuilder sb = new StringBuilder();
        Class<?> type = this.property.getPropertyType();
        if (type.isArray()) {
            for (Object o : objList) {
                Object value = this.property.getValue(o);
                if (value != null) {
                    sb.append(getArrayString(value, type.getComponentType())).append(";");
                }
            }
        } else {
            for (Object o : objList) {
                Object value = this.property.getValue(o);
                if (value != null) {
                    sb.append(value.toString()).append(";");
                }
            }
        }
        Field f = new org.apache.lucene.document.Field(this.prefix + this.property.getName(), sb.toString(), store ? Field.Store.YES : Field.Store.NO, analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED);
        f.setBoost(boost);
        doc.add(f);
    }
}