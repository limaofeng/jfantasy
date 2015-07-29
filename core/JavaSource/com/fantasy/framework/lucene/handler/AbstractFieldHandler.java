package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.mapper.DataType;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;

public abstract class AbstractFieldHandler implements FieldHandler {
    protected static final String JOIN = ";";
    protected Object obj;
    protected Field field;
    protected String prefix;

    protected AbstractFieldHandler(Object obj, Field field, String prefix) {
        this.obj = obj;
        this.field = field;
        this.prefix = prefix;
    }

    protected String getArrayString(Object value, Class<?> type) {
        StringBuilder sb = new StringBuilder();
        if (DataType.isDate(type)) {
            Date[] arr = (Date[]) (Date[]) value;
            for (Date e : arr) {
                sb.append(e.getTime()).append(";");
            }
        } else if (DataType.isTimestamp(type)) {
            Timestamp[] arr = (Timestamp[]) (Timestamp[]) value;
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