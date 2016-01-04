package org.jfantasy.framework.lucene.cache;

import org.jfantasy.framework.lucene.exception.IdException;
import org.jfantasy.framework.lucene.exception.PropertyException;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.reflect.Property;
import org.apache.log4j.Logger;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class PropertysCache {
    private static final Logger LOGGER = Logger.getLogger(PropertysCache.class);

    private static PropertysCache instance = new PropertysCache();

    private PropertysCache() {
    }

    public static PropertysCache getInstance() {
        return instance;
    }

    public Property[] get(Class<?> clazz) {
        return ClassUtil.getPropertys(clazz);
    }

    public <T extends Annotation> Property[] filter(Class<?> clazz, Class<T> tClass) {
        List<Property> properties = new ArrayList<Property>();
        for (Property p : this.get(clazz)) {
            if (p.getAnnotation(tClass) != null) {
                properties.add(p);
            }
        }
        return properties.toArray(new Property[properties.size()]);
    }

    public Property getIdProperty(Class<?> clazz) throws IdException {
        Property result = null;
        Property[] properties = get(clazz);
        for (Property p : properties) {
            if (p.getAnnotation(Id.class) != null) {
                result = p;
                break;
            }
        }
        if (result == null) {
            throw new IdException(clazz.getName() + " does not contain @Id field.");
        }
        return result;
    }

    public String getIdPropertyName(Class<?> clazz) {
        String name = null;
        Property f = null;
        try {
            f = getIdProperty(clazz);
        } catch (IdException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        if (f != null) {
            name = f.getName();
        }
        return name;
    }

    public Property getProperty(Class<?> clazz, String fieldName) throws PropertyException {
        if (fieldName.contains(".")) {
            return PropertysCache.getInstance().getProperty(clazz, fieldName.split("\\.")[0]);
        }
        Property property = null;
        Property[] properties = get(clazz);
        for (Property p : properties) {
            if (p.getName().equals(fieldName)) {
                property = p;
                break;
            }
        }
        if (property == null) {
            throw new PropertyException("Field '" + fieldName + "' does not exists!");
        }
        return property;
    }

}