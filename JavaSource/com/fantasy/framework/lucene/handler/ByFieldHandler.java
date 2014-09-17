package com.fantasy.framework.lucene.handler;

import java.util.Iterator;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import com.fantasy.framework.lucene.mapper.FieldUtil;

public abstract class ByFieldHandler extends PropertyFieldHandler {
	protected ByFieldHandler(Object obj, java.lang.reflect.Field field, String prefix) {
		super(obj, field, prefix);
	}

	protected void processList(List<?> objList, Document doc, boolean analyze, boolean store, float boost) {
		StringBuilder sb = new StringBuilder();
		Class<?> type = this.field.getType();
		if (type.isArray()) {
			for (Iterator<?> i$ = objList.iterator(); i$.hasNext();) {
				Object o = i$.next();
				Object value = FieldUtil.get(o, this.field);
				if (value != null)
					sb.append(getArrayString(value, type.getComponentType())).append(";");
			}
		} else {
			for (Iterator<?> i$ = objList.iterator(); i$.hasNext();) {
				Object o = i$.next();
				Object value = FieldUtil.get(o, this.field);
				if (value != null) {
					sb.append(value.toString()).append(";");
				}
			}
		}
		org.apache.lucene.document.Field f = new org.apache.lucene.document.Field(new StringBuilder().append(this.prefix).append(this.field.getName()).toString(), sb.toString(), store ? Field.Store.YES : Field.Store.NO, analyze ? Field.Index.ANALYZED : Field.Index.NOT_ANALYZED);

		f.setBoost(boost);
		doc.add(f);
	}
}