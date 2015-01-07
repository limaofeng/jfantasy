package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.lucene.mapper.FieldUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.document.NumericField;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
		if (type.isArray()){
            processArray(doc, analyze, store, boost);
        }else{
            processPrimitive(doc, analyze, store, boost);
        }
	}

	private void processArray(Document doc, boolean analyze, boolean store, float boost) {
		Object objValue = FieldUtil.get(this.obj, this.field);
		if (objValue == null) {
			return;
		}
		Class<?> type = this.field.getType();
		String fieldName = new StringBuilder().append(this.prefix).append(this.field.getName()).toString();
		org.apache.lucene.document.Field f = new org.apache.lucene.document.Field(fieldName, getArrayString(objValue, type.getComponentType()), store ? Field.Store.YES : Field.Store.NO, analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED);

		f.setBoost(boost);
		doc.add(f);
	}

	private void processPrimitive(Document doc, boolean analyze, boolean store, float boost) {
		Object objValue = FieldUtil.get(this.obj, this.field);
		if (objValue == null) {
			return;
		}
		Class<?> type = this.field.getType();
		String fieldName = new StringBuilder().append(this.prefix).append(this.field.getName()).toString();
		Fieldable f = null;
		if (DataType.isString(type)) {
			f = new org.apache.lucene.document.Field(fieldName, objValue.toString(), store ? Field.Store.YES : Field.Store.NO, analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED);
		} else if ((DataType.isBoolean(type)) || (DataType.isBooleanObject(type))) {
			f = new org.apache.lucene.document.Field(fieldName, objValue.toString(), store ? Field.Store.YES : Field.Store.NO, Field.Index.NOT_ANALYZED);
		} else if ((DataType.isChar(type)) || (DataType.isCharObject(type))) {
			f = new org.apache.lucene.document.Field(fieldName, objValue.toString(), store ? Field.Store.YES : Field.Store.NO, Field.Index.NOT_ANALYZED);
		} else if ((DataType.isInteger(type)) || (DataType.isIntegerObject(type))) {
			int v = Integer.parseInt(objValue.toString());
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setIntValue(v);
		} else if ((DataType.isLong(type)) || (DataType.isLongObject(type))) {
			long v = Long.parseLong(objValue.toString());
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setLongValue(v);
		} else if ((DataType.isShort(type)) || (DataType.isShortObject(type))) {
			short v = Short.parseShort(objValue.toString());
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setIntValue(v);
		} else if ((DataType.isFloat(type)) || (DataType.isFloatObject(type))) {
			float v = Float.parseFloat(objValue.toString());
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setFloatValue(v);
		} else if ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) {
			double v = Double.parseDouble(objValue.toString());
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setDoubleValue(v);
		} else if (DataType.isDate(type)) {
			Date date = (Date) objValue;
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setLongValue(date.getTime());
		} else if (DataType.isTimestamp(type)) {
			Timestamp ts = (Timestamp) objValue;
			f = new NumericField(fieldName, store ? Field.Store.YES : Field.Store.NO, true).setLongValue(ts.getTime());
		} else if ((DataType.isSet(type)) || (DataType.isList(type))) {
			Collection<?> coll = (Collection<?>) objValue;
			StringBuilder sb = new StringBuilder();
			for (Iterator<?> i$ = coll.iterator(); i$.hasNext();) {
				Object o = i$.next();
				sb.append(o).append(";");
			}
			f = new org.apache.lucene.document.Field(fieldName, sb.toString(), store ? Field.Store.YES : Field.Store.NO, analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED);
		} else if (DataType.isMap(type)) {
			Map<?, ?> map = (Map<?, ?>) objValue;
			StringBuilder sb = new StringBuilder();
			for (Map.Entry<?, ?> entry : map.entrySet()) {
				sb.append(entry.getValue()).append(";");
			}
			f = new org.apache.lucene.document.Field(fieldName, sb.toString(), store ? Field.Store.YES : Field.Store.NO, analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED);
		}
		if (f != null) {
			f.setBoost(boost);
			doc.add(f);
		}
	}
}
