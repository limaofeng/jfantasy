package com.fantasy.framework.util.common;

//import com.opensymphony.xwork2.util.ClassLoaderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.SystemPropertyUtils;

import java.util.Properties;

/**
 * Properties的操作的工具类,为Properties提供一个代理增加相关工具方法如
 * getRequiredString(),getInt(),getBoolean()等方法
 */
public class PropertiesHelper {

    public static final Log logger = LogFactory.getLog(PropertiesHelper.class);

    public static final PropertiesHelper nullPropertiesHelper = new PropertiesHelper(new Properties());

    Properties p;

    public static PropertiesHelper load(String propertiesPath) {
//        try {
            Properties props = new Properties();
            //TODO 该问题必须解决
            /*
            Iterator<URL> urls = ClassLoaderUtil.getResources(propertiesPath, PropertiesHelper.class, true);
            Properties props = new Properties();
            while (urls.hasNext()) {
                URL url = urls.next();
                Properties _props = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
                for (Map.Entry entry : _props.entrySet()) {
                    if (!props.containsKey(entry.getKey())) {
                        props.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            */
            return new PropertiesHelper(props);
//        } catch (IOException e) {
//            logger.error(e.getMessage(), e);
//            return nullPropertiesHelper;
//        }
    }

    public PropertiesHelper(Properties p) {
        setProperties(p);
    }

    public Properties getProperties() {
        return p;
    }

    public void setProperties(Properties props) {
        if (props == null){
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
}
