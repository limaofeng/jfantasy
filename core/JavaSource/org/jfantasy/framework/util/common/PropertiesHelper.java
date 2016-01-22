package org.jfantasy.framework.util.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.SystemPropertyUtils;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Properties的操作的工具类,为Properties提供一个代理增加相关工具方法如
 * getRequiredString(),getInt(),getBoolean()等方法
 */
public class PropertiesHelper {

    public static final Log LOGGER = LogFactory.getLog(PropertiesHelper.class);

    public static final PropertiesHelper nullPropertiesHelper = new PropertiesHelper(new Properties());

    private List<Properties> propertiesList = new ArrayList<Properties>();
    private ConcurrentMap<String, Properties> propertiesCache = new ConcurrentHashMap<String, Properties>();

    public static PropertiesHelper load(String propertiesPath) {
        try {
            Iterator<URL> urls = getResources(propertiesPath, PropertiesHelper.class, true);
            PropertiesHelper helper = new PropertiesHelper();
            while (urls.hasNext()) {
                URL url = urls.next();
                helper.add(PropertiesLoaderUtils.loadProperties(new UrlResource(url)));
            }
            return helper;
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return nullPropertiesHelper;
        }
    }

    private void add(Properties properties) {
        this.propertiesList.add(properties);
    }

    public PropertiesHelper(Properties... ps) {
        for (Properties p : ps) {
            this.add(p);
        }
    }

    public Properties getProperties(String... ignorePropertyNames) {
        Properties props = new Properties();
        String cacheKey = Arrays.toString(ignorePropertyNames);
        if (propertiesCache.containsKey(cacheKey)) {
            return propertiesCache.get(cacheKey);
        }
        for (Properties eProps : propertiesList) {
            for (Map.Entry entry : eProps.entrySet()) {
                if (ObjectUtil.exists(ignorePropertyNames, entry.getKey())) {
                    continue;
                }
                if (!props.containsKey(entry.getKey())) {
                    props.put(entry.getKey(), entry.getValue());
                }
            }
        }
        propertiesCache.put(cacheKey, props);
        return props;
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
        String value = getProperties().getProperty(key, defaultValue);
        return StringUtil.isNotBlank(value) ? SystemPropertyUtils.resolvePlaceholders(value, true) : value;
    }

    public String getProperty(String key) {
        String value = getProperties().getProperty(key);
        return StringUtil.isNotBlank(value) ? SystemPropertyUtils.resolvePlaceholders(value, true) : value;
    }

    public String[] getMergeProperty(String key) {
        List<String> values = new ArrayList<String>();
        for (Properties eProps : propertiesList) {
            for (Object pkey : eProps.keySet()) {
                if (key.equals(pkey)) {
                    String value = eProps.getProperty(key);
                    if (StringUtil.isNotBlank(value)) {
                        values.add(eProps.getProperty(key));
                    }
                }
            }
        }
        return values.toArray(new String[values.size()]);
    }

    public Object setProperty(String key, String value) {
        return getProperties().setProperty(key, value);
    }

    public void clear() {
        getProperties().clear();
    }

    public int size() {
        return getProperties().size();
    }

    public String toString() {
        return getProperties().toString();
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
                if (!enums.isEmpty()) {
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
