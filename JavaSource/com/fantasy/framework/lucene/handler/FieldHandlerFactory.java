package com.fantasy.framework.lucene.handler;

import java.lang.reflect.Field;

import javax.persistence.Id;

import com.fantasy.framework.lucene.annotations.IndexEmbed;
import com.fantasy.framework.lucene.annotations.IndexEmbedList;
import com.fantasy.framework.lucene.annotations.IndexProperty;
import com.fantasy.framework.lucene.annotations.IndexRef;
import com.fantasy.framework.lucene.annotations.IndexRefList;

public class FieldHandlerFactory {
	private static final String DOT = ".";

	public static FieldHandler create(Object obj, Field f, String prefix) {
		FieldHandler handler = null;
		if (f.getAnnotation(Id.class) != null) {
			handler = new IdFieldHandler(obj, f, "");
		} else if (f.getAnnotation(IndexProperty.class) != null) {
			handler = new PropertyFieldHandler(obj, f, prefix);
		} else if (f.getAnnotation(IndexEmbed.class) != null) {
			handler = new EmbedFieldHandler(obj, f, f.getName() + DOT);
		} else if (f.getAnnotation(IndexEmbedList.class) != null) {
			handler = new EmbedListFieldHandler(obj, f, f.getName() + DOT);
		} else if (f.getAnnotation(IndexRef.class) != null) {
			handler = new RefFieldHandler(obj, f, f.getName() + DOT);
		} else if (f.getAnnotation(IndexRefList.class) != null) {
			handler = new RefListFieldHandler(obj, f, f.getName() + DOT);
		}
		return handler;
	}
}
