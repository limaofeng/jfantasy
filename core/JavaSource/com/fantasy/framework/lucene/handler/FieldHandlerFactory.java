package com.fantasy.framework.lucene.handler;

import com.fantasy.framework.lucene.annotations.*;
import com.fantasy.framework.util.reflect.Property;

import javax.persistence.Id;

public class FieldHandlerFactory {
    private FieldHandlerFactory() {
    }

    private static final String DOT = ".";

    public static boolean isHandler(Property p) {
        Class[] tClases = {Id.class, IndexProperty.class, IndexEmbed.class, IndexEmbedList.class, IndexRef.class, IndexRefList.class};
        for (Class tClass : tClases) {
            if (p.getAnnotation(tClass) != null) {
                return true;
            }
        }
        return false;
    }

    public static FieldHandler create(Object obj, Property p, String prefix) {
        FieldHandler handler = null;
        if (p.getAnnotation(Id.class) != null) {
            handler = new IdFieldHandler(obj, p, "");
        } else if (p.getAnnotation(IndexProperty.class) != null) {
            handler = new PropertyFieldHandler(obj, p, prefix);
        } else if (p.getAnnotation(IndexEmbed.class) != null) {
            handler = new EmbedFieldHandler(obj, p, p.getName() + DOT);
        } else if (p.getAnnotation(IndexEmbedList.class) != null) {
            handler = new EmbedListFieldHandler(obj, p, p.getName() + DOT);
        } else if (p.getAnnotation(IndexRef.class) != null) {
            handler = new RefFieldHandler(obj, p, p.getName() + DOT);
        } else if (p.getAnnotation(IndexRefList.class) != null) {
            handler = new RefListFieldHandler(obj, p, p.getName() + DOT);
        }
        return handler;
    }
}
