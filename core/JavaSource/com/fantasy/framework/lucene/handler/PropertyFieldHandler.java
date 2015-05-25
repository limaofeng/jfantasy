package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import org.apache.lucene.document.*;
import org.apache.lucene.index.FieldInfo;
import org.apache.lucene.index.IndexableField;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

public class PropertyFieldHandler extends AbstractFieldHandler {
    public PropertyFieldHandler(Object obj, java.lang.reflect.Field field, String prefix) {
        super(obj, field, prefix);
    }

    public void handle(Document doc) {
        IndexProperty ip = this.field.getAnnotation(IndexProperty.class);
        process(doc, ip.analyze(), ip.store(), ip.boost());
    }

    protected void process(Document doc, boolean analyze, boolean store, float boost) {
        Class<?> type = this.field.getType();
        if (type.isArray()) {
            processArray(doc, analyze, store, boost);
        } else {
            processPrimitive(doc, analyze, store, boost);
        }
    }

    private void processArray(Document doc, boolean analyze, boolean store, float boost) {
        Object objValue = FieldUtil.get(this.obj, this.field);
        if (objValue == null) {
            return;
        }
        Class<?> type = this.field.getType();
        String fieldName = this.prefix + this.field.getName();

        Field f = new Field(fieldName, getArrayString(objValue, type.getComponentType()), getFieldType(analyze, store));

        f.setBoost(boost);
        doc.add(f);
    }

    private void processPrimitive(Document doc, boolean analyze, boolean store, float boost) {
        Object objValue = FieldUtil.get(this.obj, this.field);
        if (objValue == null) {
            return;
        }
        Class<?> type = this.field.getType();
        String fieldName = this.prefix + this.field.getName();
        IndexableField f = null;
        if (DataType.isString(type)) {
            f = new Field(fieldName, objValue.toString(), getFieldType(analyze, store));
        } else if ((DataType.isBoolean(type)) || (DataType.isBooleanObject(type))) {
            f = new StringField(fieldName, objValue.toString(), store ? Field.Store.YES : Field.Store.NO);
        } else if ((DataType.isChar(type)) || (DataType.isCharObject(type))) {
            f = new StringField(fieldName, objValue.toString(), store ? Field.Store.YES : Field.Store.NO);
        } else if ((DataType.isInteger(type)) || (DataType.isIntegerObject(type))) {
            int v = Integer.parseInt(objValue.toString());
            f = new IntField(fieldName, v, store ? Field.Store.YES : Field.Store.NO);
        } else if ((DataType.isLong(type)) || (DataType.isLongObject(type))) {
            long v = Long.parseLong(objValue.toString());
            f = new LongField(fieldName, v, store ? Field.Store.YES : Field.Store.NO);
        } else if ((DataType.isShort(type)) || (DataType.isShortObject(type))) {
            short v = Short.parseShort(objValue.toString());
            f = store ? new SortedNumericDocValuesField(fieldName, v) : new NumericDocValuesField(fieldName, v);
        } else if ((DataType.isFloat(type)) || (DataType.isFloatObject(type))) {
            float v = Float.parseFloat(objValue.toString());
            f = new FloatField(fieldName, v, store ? Field.Store.YES : Field.Store.NO);
        } else if ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) {
            double v = Double.parseDouble(objValue.toString());
            f = new DoubleField(fieldName, v, store ? Field.Store.YES : Field.Store.NO);
        } else if (DataType.isDate(type)) {
            Date date = (Date) objValue;
            f = store ? new SortedNumericDocValuesField(fieldName, date.getTime()) : new NumericDocValuesField(fieldName, date.getTime());
        } else if (DataType.isTimestamp(type)) {
            Timestamp ts = (Timestamp) objValue;
            f = store ? new SortedNumericDocValuesField(fieldName, ts.getTime()) : new NumericDocValuesField(fieldName, ts.getTime());
        } else if ((DataType.isSet(type)) || (DataType.isList(type))) {
            Collection<?> coll = (Collection<?>) objValue;
            StringBuilder sb = new StringBuilder();
            for (Object o : coll) {
                sb.append(o).append(";");
            }
            f = new Field(fieldName, sb.toString(), getFieldType(analyze, store));
        } else if (DataType.isMap(type)) {
            Map<?, ?> map = (Map<?, ?>) objValue;
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                sb.append(entry.getValue()).append(";");
            }
            f = new Field(fieldName, sb.toString(), getFieldType(analyze, store));
        }
        if (f != null) {
            ((Field) f).setBoost(boost);
            doc.add(f);
        }
    }

    public static FieldType getFieldType(boolean analyze, boolean store) {
        FieldType fieldType = new FieldType();
        fieldType.setDocValueType(FieldInfo.DocValuesType.BINARY);
        fieldType.setStored(store);
        fieldType.setIndexed(analyze);
        return fieldType;
    }

}
