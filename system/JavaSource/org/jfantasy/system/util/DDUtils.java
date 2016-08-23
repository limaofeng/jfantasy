package org.jfantasy.system.util;

import org.jfantasy.framework.spring.SpringContextUtil;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.system.bean.DataDictionary;
import org.jfantasy.system.bean.DataDictionaryKey;
import org.jfantasy.system.bean.DataDictionaryType;
import org.jfantasy.system.service.DataDictionaryService;

/**
 * 数据字典工具类
 */
public class DDUtils {

    private static DataDictionaryService ddService;

    private static DataDictionaryService ddService() {
        if (ddService == null) {
            ddService = SpringContextUtil.getBeanByType(DataDictionaryService.class);
        }
        return ddService;
    }

    /**
     * 设置数据字典项
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, int value) {
        set(key, String.valueOf(value));
    }

    /**
     * 设置数据字典项
     *
     * @param key   键
     * @param value 值
     */
    public static void set(String key, String value) {
        DataDictionary dd = new DataDictionary();
        dd.setKey(DataDictionaryKey.newInstance(key));
        DataDictionaryType type = ddService().getDataDictionaryType(dd.getKey().getType());
        if (type == null) {
            type = new DataDictionaryType();
            type.setCode(dd.getKey().getType());
            type.setName(dd.getKey().getType());
            ddService().save(type);
        }
        dd.setName(value);
        ddService().save(dd);
    }

    public static void set(String key, String name, String value) {
        DataDictionary dd = new DataDictionary();
        dd.setKey(DataDictionaryKey.newInstance(key));
        DataDictionaryType type = ddService().getDataDictionaryType(dd.getKey().getType());
        if (type == null) {
            type = new DataDictionaryType();
            type.setCode(dd.getKey().getType());
            type.setName(dd.getKey().getType());
            ddService().save(type);
        }
        dd.setName(name);
        dd.setDescription(value);
        ddService().save(dd);
    }

    /**
     * 获取字段项值
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        if (StringUtil.isBlank(key) || !key.contains(":")) {
            return null;
        }
        DataDictionary dataDictionary = ddService().get(DataDictionaryKey.newInstance(key));
        return dataDictionary == null ? null : dataDictionary.getName();
    }

    /**
     * value 对应 description 字段
     *
     * @param key 键
     * @return value
     */
    public static String getValue(String key) {
        if (StringUtil.isBlank(key) || !key.contains(":")) {
            return null;
        }
        DataDictionary dataDictionary = ddService().get(DataDictionaryKey.newInstance(key));
        return dataDictionary == null ? null : dataDictionary.getDescription();
    }

    /**
     * 获取字段项值
     *
     * @param key   键
     * @param clazz 类型
     * @return 值
     */
    public static <T> T get(String key, Class<T> clazz) {
        String value = get(key);
        if (!ClassUtil.isBasicType(clazz)) {
            throw new ClassCastException(value + " 不能正确的转换为 " + clazz);
        }
        return value == null ? null : (T) ClassUtil.newInstance(clazz.isPrimitive() ? ClassUtil.resolvePrimitiveIfNecessary(clazz) : clazz, value);
    }

    /**
     * 获取字段项值
     *
     * @param key          键
     * @param defaultValue 默认值
     * @return 值
     */
    public static String get(String key, String defaultValue) {
        return ObjectUtil.defaultValue(get(key), defaultValue);
    }

    /**
     * 获取字段项值
     *
     * @param key          键
     * @param clazz        类型
     * @param defaultValue 默认值
     * @return 值
     */
    public static <T> T get(String key, Class<T> clazz, T defaultValue) {
        return ObjectUtil.defaultValue(get(key, clazz), defaultValue);
    }

    /**
     * 删除字典项
     *
     * @param key 键
     */
    public static void remove(String key) {
        ddService.delete(key);
    }

}