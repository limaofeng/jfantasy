package com.fantasy.framework.lucene.cache;

import com.fantasy.framework.lucene.exception.FieldException;
import com.fantasy.framework.lucene.exception.IdException;
import org.apache.log4j.Logger;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class FieldsCache {
    private static final Logger LOGGER = Logger.getLogger(FieldsCache.class);

    private static FieldsCache instance = new FieldsCache();
    private Map<String, Field[]> cache;

    private FieldsCache() {
        this.cache = new ConcurrentHashMap<String, Field[]>();
    }

    public static FieldsCache getInstance() {
        return instance;
    }

    private Field[] getAllFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<Field>();
        allFields.addAll(filterFields(clazz.getDeclaredFields()));
        Class<?> parent = clazz.getSuperclass();
        while ((parent != null) && (parent != Object.class)) {
            allFields.addAll(filterFields(parent.getDeclaredFields()));
            parent = parent.getSuperclass();
        }
        return (Field[]) allFields.toArray(new Field[allFields.size()]);
    }

    private List<Field> filterFields(Field[] fields) {
        List<Field> result = new ArrayList<Field>();
        for (Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers())) {
                field.setAccessible(true);
                result.add(field);
            }
        }
        return result;
    }

    public Field[] get(Class<?> clazz) {
        Field[] fields = null;
        String name = clazz.getName();
        if (this.cache.containsKey(name)) {
            fields = (Field[]) this.cache.get(name);
        } else {
            fields = getAllFields(clazz);
            this.cache.put(name, fields);
        }
        return fields;
    }

    public Field getIdField(Class<?> clazz) throws IdException {
        Field result = null;
        Field[] fields = get(clazz);
        for (Field f : fields) {
            if (f.getAnnotation(Id.class) != null) {
                result = f;
                break;
            }
        }
        if (result == null) {
            throw new IdException(clazz.getName() + " does not contain @Id field.");
        }
        return result;
    }

    public String getIdFieldName(Class<?> clazz) {
        String name = null;
        Field f = null;
        try {
            f = getIdField(clazz);
        } catch (IdException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        if (f != null) {
            name = f.getName();
        }
        return name;
    }

    public Field getField(Class<?> clazz, String fieldName) throws FieldException {
        if (fieldName.contains(".")) {
            return FieldsCache.getInstance().getField(clazz, fieldName.split("\\.")[0]);
        }
        Field field = null;
        Field[] fields = get(clazz);
        for (Field f : fields) {
            if (f.getName().equals(fieldName)) {
                field = f;
                break;
            }
        }
        if (field == null) {
            throw new FieldException("Field '" + fieldName + "' does not exists!");
        }
        return field;
    }

}