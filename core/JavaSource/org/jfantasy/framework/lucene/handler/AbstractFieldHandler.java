package org.jfantasy.framework.lucene.handler;

import org.jfantasy.framework.lucene.mapper.DataType;
import org.jfantasy.framework.util.reflect.Property;

import java.lang.reflect.Array;
import java.sql.Timestamp;
import java.util.Date;

public abstract class AbstractFieldHandler implements FieldHandler {
    protected static final String JOIN = ";";
    protected Object obj;
    protected Property property;
    protected String prefix;

    protected AbstractFieldHandler(Object obj, Property property, String prefix) {
        this.obj = obj;
        this.property = property;
        this.prefix = prefix;
    }

    protected String getArrayString(Object value, Class<?> type) {
        StringBuilder sb = new StringBuilder();
        if (DataType.isDate(type)) {
            Date[] arr = (Date[]) value;
            for (Date e : arr) {
                sb.append(e.getTime()).append(";");
            }
        } else if (DataType.isTimestamp(type)) {
            Timestamp[] arr = (Timestamp[]) value;
            for (Timestamp e : arr) {
                sb.append(e.getTime()).append(";");
            }
        } else {
            int len = Array.getLength(value);
            for (int i = 0; i < len; i++) {
                sb.append(Array.get(value, i).toString()).append(";");
            }
        }
        return sb.toString();
    }
}