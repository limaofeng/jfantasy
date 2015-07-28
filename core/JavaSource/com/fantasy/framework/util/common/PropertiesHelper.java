package com.fantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Properties的操作的工具类,为Properties提供一个代理增加相关工具方法如
 * getRequiredString(),getInt(),getBoolean()等方法
 */
public class PropertiesHelper {

    public static final Log LOGGER = LogFactory.getLog(PropertiesHelper.class);

    public static final PropertiesHelper nullPropertiesHelper = new PropertiesHelper(new Properties());

    Properties p;

    public static PropertiesHelper load(String propertiesPath) {
        try {
            Iterator<URL> urls = getResources(propertiesPath, PropertiesHelper.class, true);
            Properties props = new Properties();
            while (urls.hasNext()) {
                URL url = urls.next();
                Properties eProps = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
                for (Map.Entry entry : eProps.entrySet()) {
                    if (!props.containsKey(entry.getKey())) {
                        props.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            return new PropertiesHelper(props);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return nullPropertiesHelper;
        }
    }

    public PropertiesHelper(Properties p) {
        setProperties(p);
    }

    public Properties getProperties() {
        return p;
    }

    public void setProperties(Properties props) {
        if (props == null) {
            throw new IllegalArgumentException("properties must be not null");
        }
        this.p = props;
    }

    public String getRequiredString(String key) {
        String value = getProperty(key);
        if (StringUtil.isBlank(value)) {
            throw new IllegalStateException("required property is blank by key=" + key);
        }
        return value;
    }

    public String getNullIfBlank(String key) {
        String value = getProperty(key);
        if (StringUtil.isBlank(value)) {
            return null;
        }
        return value;
    }

    public String getNullIfEmpty(String key) {
        String value = getProperty(key);
        if (value == null || "".equals(value)) {
            return null;
        }
        return value;
    }

    public Integer getInteger(String key) {
        String v = getProperty(key);
        if (v == null) {
            return null;
        }
        return Integer.parseInt(v);
    }

    public int getInt(String key, int defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Integer.parseInt(getRequiredString(key));
    }

    public int getRequiredInt(String key) {
        return Integer.parseInt(getRequiredString(key));
    }

    public Long getLong(String key) {
        if (getProperty(key) == null) {
            return null;
        }
        return Long.parseLong(getRequiredString(key));
    }

    public long getLong(String key, long defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Long.parseLong(getRequiredString(key));
    }

    public Long getRequiredLong(String key) {
        return Long.parseLong(getRequiredString(key));
    }

    public Boolean getBoolean(String key) {
        return getProperty(key) != null && Boolean.parseBoolean(getRequiredString(key));
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Boolean.parseBoolean(getRequiredString(key));
    }

    public boolean getRequiredBoolean(String key) {
        return Boolean.parseBoolean(getRequiredString(key));
    }

    public Float getFloat(String key) {
        if (getProperty(key) == null) {
            return null;
        }
        return Float.parseFloat(getRequiredString(key));
    }

    public float getFloat(String key, float defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Float.parseFloat(getRequiredString(key));
    }

    public Float getRequiredFloat(String key) {
        return Float.parseFloat(getRequiredString(key));
    }

    public Double getDouble(String key) {
        if (getProperty(key) == null) {
            return null;
        }
        return Double.parseDouble(getRequiredString(key));
    }

    public double getDouble(String key, double defaultValue) {
        if (getProperty(key) == null) {
            return defaultValue;
        }
        return Double.parseDouble(getRequiredString(key));
    }

    public Double getRequiredDouble(String key) {
        return Double.parseDouble(getRequiredString(key));
    }

    public Object setProperty(String key, Object value) {
        return setProperty(key, String.valueOf(value));
    }

    public String getProperty(String key, String defaultValue) {
        String value = p.getProperty(key, defaultValue);
        return StringUtil.isNotBlank(value) ? SystemPropertyUtils.resolvePlaceholders(value, true) : value;
    }

    public String getProperty(String key) {
        String value = p.getProperty(key);
        return StringUtil.isNotBlank(value) ? SystemPropertyUtils.resolvePlaceholders(value, true) : value;
    }

    public Object setProperty(String key, String value) {
        return p.setProperty(key, value);
    }

    public void clear() {
        p.clear();
    }

    public int size() {
        return p.size();
    }

    public String toString() {
        return p.toString();
    }

    public static Iterator<URL> getResources(String resourceName, Class callingClass, boolean aggregate) throws IOException {

        AggregateIterator<URL> iterator = new AggregateIterator<URL>();

        iterator.addEnumeration(Thread.currentThread().getContextClassLoader().getResources(resourceName));

        if (!iterator.hasNext() || aggregate) {
            iterator.addEnumeration(PropertiesHelper.class.getClassLoader().getResources(resourceName));
        }

        if (!iterator.hasNext() || aggregate) {
            ClassLoader cl = callingClass.getClassLoader();

            if (cl != null) {
                iterator.addEnumeration(cl.getResources(resourceName));
            }
        }

        if (!iterator.hasNext() && (resourceName != null) && ((resourceName.length() == 0) || (resourceName.charAt(0) != '/'))) {
            return getResources('/' + resourceName, callingClass, aggregate);
        }

        return iterator;
    }

    static class AggregateIterator<E> implements Iterator<E> {

        LinkedList<Enumeration<E>> enums = new LinkedList<Enumeration<E>>();
        Enumeration<E> cur = null;
        E next = null;
        Set<E> loaded = new HashSet<E>();

        public AggregateIterator<E> addEnumeration(Enumeration<E> e) {
            if (e.hasMoreElements()) {
                if (cur == null) {
                    cur = e;
                    next = e.nextElement();
                    loaded.add(next);
                } else {
                    enums.add(e);
                }
            }
            return this;
        }

        public boolean hasNext() {
            return next != null;
        }

        public E next() {
            if (next != null) {
                E prev = next;
                next = loadNext();
                return prev;
            } else {
                throw new NoSuchElementException();
            }
        }

        private Enumeration<E> determineCurrentEnumeration() {
            if (cur != null && !cur.hasMoreElements()) {
                if (enums.size() > 0) {
                    cur = enums.removeLast();
                } else {
                    cur = null;
                }
            }
            return cur;
        }

        private E loadNext() {
            if (determineCurrentEnumeration() != null) {
                E tmp = cur.nextElement();
                int loadedSize = loaded.size();
                while (loaded.contains(tmp)) {
                    tmp = loadNext();
                    if (tmp == null || loaded.size() > loadedSize) {
                        break;
                    }
                }
                if (tmp != null) {
                    loaded.add(tmp);
                }
                return tmp;
            }
            return null;

        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
