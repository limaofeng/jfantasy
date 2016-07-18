package org.jfantasy.framework.util.common;

import org.jfantasy.framework.dao.mybatis.keygen.GUIDKeyGenerator;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.lucene.BuguIndex;
import org.jfantasy.framework.spring.SpELUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.jfantasy.framework.util.reflect.Property;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.hibernate.collection.internal.PersistentBag;
import org.springframework.expression.Expression;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.text.Collator;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("unchecked")
public final class ObjectUtil {

    private ObjectUtil() {
    }

    private static final Log LOGGER = LogFactory.getLog(ObjectUtil.class);

    private static final ConcurrentMap<String, Comparator<?>> comparatorMap = new ConcurrentHashMap<String, Comparator<?>>();

    /**
     * 克隆对象,调用org.apache.commons.beanutils.BeanUtils.cloneBean(object);方法实现克隆
     *
     * @param object 将要克隆的对象
     * @return 返回的对象
     */
    public static <T> T clone(T object) {
        if (object == null) {
            return null;
        }
        try {
            if (object instanceof Number) {
                return object;
            } else if (object instanceof String) {
                return object;
            } else if (object instanceof Map) {
                Map<Object, Object> cloneMap = new HashMap<Object, Object>();
                Map<Object, Object> map = (Map<Object, Object>) object;
                for (Map.Entry<Object, Object> entry : map.entrySet()) {
                    cloneMap.put(clone(entry.getKey()), clone(entry.getValue()));
                }
                return (T) cloneMap;
            } else if (object instanceof List) {
                List<Object> cloneList = new ArrayList<Object>();
                List<Object> list = (List<Object>) object;
                for (Object l : list) {
                    cloneList.add(clone(l));
                }
                return (T) cloneList;
            } else {
                return (T) BeanUtils.cloneBean(object);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new IgnoreException(e.getMessage());
        }
    }

    /**
     * 将集合对象中的 @{fieldName} 对于的值转换为字符串以 @{sign} 连接
     *
     * @param <T>       泛型
     * @param objs      集合
     * @param fieldName 支持ognl表达式
     * @param sign      连接符
     * @return T
     */
    public static <T> String toString(List<T> objs, String fieldName, String sign) {
        AtomicReference<StringBuffer> stringBuffer = new AtomicReference<StringBuffer>(new StringBuffer());
        for (T t : objs) {
            String temp = StringUtil.defaultValue(OgnlUtil.getInstance().getValue(fieldName, t), "");
            if (StringUtil.isBlank(temp)) {
                continue;
            }
            stringBuffer.get().append(sign).append(temp);
        }
        return stringBuffer.get().toString().replaceFirst(sign, "");
    }

    public static <T> String toString(T[] objs, String sign) {
        return toString(objs, null, sign);
    }

    public static <T> List<T> filter(List<T> list, String fieldName, Object... values) {
        List<T> filter = new ArrayList<T>();
        for (Object v : values) {
            T t = find(list, fieldName, v);
            if (t != null) {
                filter.add(t);
            }
        }
        return filter;
    }

    public static <T> T[] filter(T[] objs, String fieldName, Object[] values) {
        if (values.length == 1 && ClassUtil.isArray(Array.get(values, 0))) {
            values = (T[]) Array.get(values, 0);
        }
        List<T> filter = new ArrayList<T>();
        for (Object v : values) {
            T t = find(objs, fieldName, v);
            if (t != null) {
                filter.add(t);
            }
        }
        return (T[]) filter.toArray(new Object[filter.size()]);
    }

    public static <T> List<T> filter(List<T> list, String spel) {
        Expression expression = SpELUtil.getExpression(spel);
        List<T> filter = new ArrayList<T>();
        for (T v : list) {
            if (expression.getValue(SpELUtil.createEvaluationContext(v), Boolean.class)) {
                filter.add(v);
            }
        }
        return filter;
    }

    public static <T> String toString(T[] objs, String fieldName, String sign) {
        if (objs.length == 1) {
            if (ClassUtil.isArray(objs[0])) {
                return toString((T[]) objs[0], fieldName, sign);
            } else if (ClassUtil.isList(objs[0])) {
                return toString((List<T>) objs[0], fieldName, sign);
            }
        }
        AtomicReference<StringBuffer> stringBuffer = new AtomicReference<StringBuffer>(new StringBuffer());
        for (T t : objs) {
            String temp = StringUtil.isBlank(fieldName) ? t.toString() : StringUtil.defaultValue(OgnlUtil.getInstance().getValue(fieldName, t), "");
            if (StringUtil.isBlank(temp)) {
                continue;
            }
            stringBuffer.get().append(sign).append(temp);
        }
        return stringBuffer.get().toString().replaceFirst(sign, "");
    }

    public static <T, R> List<R> toFieldList(List<T> list, String fieldName, List<R> returnList) {
        for (Object t : list) {
            returnList.add((R) OgnlUtil.getInstance().getValue(fieldName, t));
        }
        return returnList;
    }

    public static <T, R> R[] toFieldArray(List<T> objs, String fieldName, Class<R> componentType) {
        R[] returnObjs = (R[]) ClassUtil.newInstance(componentType, objs.size());
        for (int i = objs.size() - 1; i > -1; i--) {
            if (objs.get(i) == null) {
                continue;
            }
            returnObjs[i] = (R) OgnlUtil.getInstance().getValue(fieldName, objs.get(i));
        }
        return returnObjs;
    }

    public static <T, R> R[] toFieldArray(T[] objs, String fieldName, Class<R> componentType) {
        return toFieldArray(objs, fieldName, (R[]) Array.newInstance(componentType, objs.length));
    }

    public static <T, R> R[] toFieldArray(T[] objs, String fieldName, R[] returnObjs) {
        if (returnObjs.length < objs.length) {
            returnObjs = (R[]) ClassUtil.newInstance(returnObjs.getClass().getComponentType(), objs.length);
        }
        for (int i = objs.length - 1; i > -1; i--) {
            returnObjs[i] = (R) ClassUtil.getValue(objs[i], fieldName);
        }
        return returnObjs;
    }

    /**
     * 返回 集合中 @{fieldName} 值最大的对象
     *
     * @param <T>       泛型
     * @param c         集合
     * @param fieldName 支持ognl表达式
     * @return T
     */
    public static <T> T getMaxObject(Collection<T> c, String fieldName) {
        T maxObject = null;
        for (T element : c) {
            if (maxObject == null) {
                maxObject = element;
            } else {
                if (compareField(maxObject, element, fieldName) == 1) {
                    maxObject = element;
                }
            }
        }
        return maxObject;
    }

    /**
     * 返回 集合中 @{fieldName} 值最小的对象
     *
     * @param <T>       泛型
     * @param c         集合
     * @param fieldName 支持ognl表达式
     * @return T
     */
    public static <T> T getMinObject(Collection<T> c, String fieldName) {
        T minObject = null;
        for (T element : c) {
            if (minObject == null) {
                minObject = element;
            } else {
                if (compareField(minObject, element, fieldName) == -1) {
                    minObject = element;
                }
            }
        }
        return minObject;
    }

    /**
     * 获取集合中 @{field} 的值为 @{value} 的对象 返回索引下标
     *
     * @param <T>   泛型
     * @param objs  原始集合
     * @param field 支持ognl表达式
     * @param value 比较值
     * @return T
     * 如果有多个只返回第一匹配的对象,比较调用对象的 equals 方法
     */
    public static <T> int indexOf(List<T> objs, String field, Object value) {
        for (int i = 0; i < objs.size(); i++) {
            Object prop = OgnlUtil.getInstance().getValue(field, objs.get(i));
            if (prop == null) {
                continue;
            }
            if (prop.equals(value)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(List<T> objs, Expression exper, Object value) {
        for (int i = 0; i < objs.size(); i++) {
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("value", value);
            if (exper.getValue(SpELUtil.createEvaluationContext(objs.get(i), data), Boolean.class)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 在集合中查找 @{field} 对于的值为 ${value} 的对象
     *
     * @param <T>   对应的泛型类型
     * @param list  原始集合
     * @param field 支持ognl表达式
     * @param value 比较值
     * @return 返回第一次匹配的对象
     */
    public static <T> T find(List<T> list, String field, Object value) {
        if (list == null) {
            return null;
        }
        for (T t : list) {
            Object v = OgnlUtil.getInstance().getValue(field, t);
            if (v == value || value.equals(v)) {
                return t;
            }
        }
        return null;
    }

    public static <T> T last(List<T> list, String field, Object value) {
        if (list == null) {
            return null;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            T t = list.get(i);
            Object v = OgnlUtil.getInstance().getValue(field, t);
            if (v == value || value.equals(v)) {
                return t;
            }
        }
        return null;
    }

    public static <T> boolean exists(List<T> list, String field, Object value) {
        return find(list, field, value) != null;
    }

    public static <T> T find(List<T> list, Expression exper, Object value) {
        if (list == null) {
            return null;
        }
        int i = indexOf(list, exper, value);
        return i >= 0 ? list.get(i) : null;
    }

    public static <T> T find(T[] list, String field, Object value) {
        if (list == null) {
            return null;
        }
        for (T t : list) {
            if (t == null) {
                continue;
            }
            Object v = OgnlUtil.getInstance().getValue(field, t);
            if (v == value || value.equals(v)) {
                return t;
            }
        }
        return null;
    }

    public static int indexOf(char[] objs, char c) {
        for (int i = 0; i < objs.length; i++) {
            if (objs[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(T[] objs, T o) {
        for (int i = 0; i < objs.length; i++) {
            if (ClassUtil.isList(o)) {
                if (!ClassUtil.isList(objs[i]) || ((List<Object>) o).isEmpty()) {
                    continue;
                }
                if (((List<Object>) o).size() != ((List<Object>) objs[i]).size()) {
                    continue;
                }
                int num = 0;
                for (Object obj : (List<Object>) o) {
                    if (indexOf((List<Object>) objs[i], obj) > -1) {
                        num++;
                    }
                }
                if (num == ((List<Object>) o).size()) {
                    return i;
                }
            } else {
                if (objs[i].equals(o)) {
                    return i;
                }

            }
        }
        return -1;
    }

    public static <T> int indexOf(List<T> objs, T o) {
        return indexOf(objs.toArray(new Object[objs.size()]), o);
    }

    public static <T> int indexOf(List<T> objs, T obj, String property) {
        for (int i = 0; i < objs.size(); i++) {
            Object value = ClassUtil.getValue(objs.get(i), property);
            if (isNull(value)) {
                continue;
            }
            if (value.equals(ClassUtil.getValue(obj, property))) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int indexOf(List<T> list, String field, String value, boolean ignoreCase) {
        for (int i = 0; i < list.size(); i++) {
            Object obj = list.get(i);
            Object prop = ClassUtil.getValue(obj, field);
            if (prop == null) {
                continue;
            }
            if (ignoreCase ? value.equalsIgnoreCase(StringUtil.nullValue(prop)) : value.equals(prop)) {
                return i;
            }
        }
        return -1;
    }

    public static void setProperties(Object obj, String fieldName, Object value) {
        try {
            BeanUtil.setValue(obj, fieldName, value);
        } catch (Exception e) {
            throw new IgnoreException(e.getMessage(), e);
        }
    }

    public static <T> List<T> sort(List<T> list, String orderField) {
        return sort(list, orderField, "asc");
    }

    /**
     * 对集合进行排序
     *
     * @param <T>        泛型
     * @param collectoin 要排序的集合
     * @param orderField 排序字段 支持ognl表达式
     * @return T
     * 默认排序方向为 asc
     */
    public static <T> Collection<T> sort(Collection<T> collectoin, String orderField) {
        return sort(collectoin, orderField, "asc");
    }

    /**
     * 对集合进行排序
     *
     * @param <T>        泛型
     * @param collectoin 要排序的集合
     * @param orderBy    排序字段 支持ognl表达式
     * @param order      排序方向 只能是 asc 与 desc
     * @return T
     */
    public static <T> Collection<T> sort(Collection<T> collectoin, String orderBy, String order) {
        List<T> list = new ArrayList<T>();
        if ((collectoin == null) || (collectoin.isEmpty())) {
            return list;
        }
        String key = collectoin.iterator().next().getClass().toString().concat("|").concat(orderBy);
        if (!comparatorMap.containsKey(key)) {
            final String orderBys = orderBy;
            comparatorMap.put(key, new Comparator<T>() {
                public int compare(Object o1, Object o2) {
                    return compareField(o1, o2, orderBys);
                }
            });
        }
        list.addAll(collectoin);
        Collections.sort(list, (Comparator<T>) comparatorMap.get(key));
        if ("desc".equalsIgnoreCase(order)) {
            Collections.reverse(list);
        }
        return list;
    }

    public static <T> List<T> sort(List<T> collectoin, String orderBy, String order) {
        return (List<T>) sort((Collection<T>) collectoin, orderBy, order);
    }

    public static <T> List<T> sort(List<T> collectoin, String[] customSort, String idFieldName) {
        if (collectoin instanceof PersistentBag) {
            List<T> dest = new ArrayList<T>(collectoin.size());
            for (T t : collectoin) {
                dest.add(t);
            }
            collectoin = dest;
        }
        Collections.sort(collectoin, new CustomSortOrderComparator(customSort, idFieldName));
        return collectoin;
    }

    /**
     * 辅助方法 比较两个值的大小
     *
     * @param o1         object1
     * @param o2         object2
     * @param orderField 支持ognl表达式
     * @return int
     * 如果返回1标示 @{o1} 大于 @{o2} <br/>
     * 如果返回0标示 @{o1} 等于 @{o2} <br/>
     * 如果返回-1标示 @{o1} 小于 @{o2}
     */
    private static int compareField(Object o1, Object o2, String orderField) {
        Object f1 = OgnlUtil.getInstance().getValue(orderField, o1);
        Object f2 = OgnlUtil.getInstance().getValue(orderField, o2);
        if (f1 == f2) {
            return -1;
        }
        if (f1 == null || f2 == null) {
            return -1;
        }
        Object[] ary = {f1, f2};
        if ((f1 instanceof String) && (f2 instanceof String)) {
            Arrays.sort(ary, Collator.getInstance(Locale.CHINA));
        } else {
            Arrays.sort(ary);
        }
        if (ary[0].equals(f1)) {
            return -1;
        }
        return 1;
    }

    public static boolean isNull(Object object) {
        return object == null;
    }

    public static boolean isNotNull(Object object) {
        return !isNull(object);
    }

    public static <T> T defaultValue(T source, T def) {
        return isNull(source) ? def : source;
    }

    public static Map<String, Object> toMap(Object data) {
        if (ClassUtil.isMap(data)) {
            return (Map<String, Object>) data;
        }
        Map<String, Object> rootMap = new HashMap<String, Object>();
        Property[] properties = ClassUtil.getPropertys(data);
        for (Property property : properties) {
            if (property.isRead()) {
                rootMap.put(property.getName(), property.getValue(data));
            }
        }
        return rootMap;
    }

    public static <T> T[] join(T[] dest, T... items) {
        if (items.length == 0) {
            return dest;
        }
        Object array = Array.newInstance(dest.getClass().getComponentType(), dest.length + items.length);
        for (int i = 0; i < dest.length; i++) {
            Array.set(array, i, dest[i]);
        }
        for (int i = 0; i < items.length; i++) {
            Array.set(array, dest.length + i, items[i]);
        }
        return (T[]) array;
    }

    /**
     * 合并集合,去除重复的项
     *
     * @param <T>  泛型
     * @param dest 源集合
     * @param orig 要合并的集合
     */
    public static <T> void join(List<T> dest, List<T> orig) {
        join(dest, orig, "");
    }

    public static <T> void join(List<T> dest, List<T> orig, String property) {
        List<T> news = new ArrayList<T>();
        for (T o : orig) {
            if (StringUtil.isNotBlank(property) && (indexOf(dest, o, property) == -1)) {
                news.add(o);
            } else if (dest.indexOf(o) == -1) {
                news.add(o);
            }
        }
        dest.addAll(news);
    }

    public static <T> void join(List<T> dest, List<T> orig, Expression exper) {
        List<T> news = new ArrayList<T>();
        for (T o : orig) {
            if (isNotNull(exper) && (indexOf(dest, exper, o) == -1)) {
                news.add(o);
            } else if (dest.indexOf(o) == -1) {
                news.add(o);
            }
        }
        dest.addAll(news);
    }


    /**
     * 判断对象是否存在于集合中
     *
     * @param <T>    泛型
     * @param list   集合
     * @param object 要判断的对象
     * @return boolean
     */
    public static <T> Boolean exists(List<T> list, T object) {
        for (Object t : list) {
            if (t.getClass().isEnum() && t.toString().equals(object)) {
                return true;
            } else if (t.equals(object)) {
                return true;
            }
        }
        return false;
    }

    public static <T> Boolean exists(T[] array, T object) {
        for (T t : array) {
            if (t.equals(object)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 从集合中删除对象返回被移除的对象,通过属性判断删除
     *
     * @param <T>      泛型
     * @param orig     源集合
     * @param property 判断的字段
     * @param value    字段对应的值
     * @return 被移除的对象
     */
    public static <T> T remove(List<T> orig, String property, Object value) {
        if (orig == null || value == null) {
            return null;
        }
        int i = indexOf(orig, property, value);
        return i == -1 ? null : orig.remove(i);
    }

    public static <T> T remove(List<T> orig, Expression exper, Object value) {
        if (orig == null || value == null) {
            return null;
        }
        int i = indexOf(orig, exper, value);
        return i == -1 ? null : orig.remove(i);
    }

    /**
     * 从数组中删除对象返回新的数组
     *
     * @param <T>  泛型
     * @param dest 数组
     * @param orig 要删除的对象
     * @return T
     */
    public static <T> T[] remove(T[] dest, T orig) {
        List<T> array = new ArrayList<T>(Arrays.asList(dest));
        while (array.indexOf(orig) != -1) {
            array.remove(orig);
        }
        return array.toArray((T[]) Array.newInstance(dest.getClass().getComponentType(), array.size()));
    }

    public static <T> T[] removeFirst(T[] dest, T orig) {
        if (indexOf(dest, orig) != -1) {
            dest = remove(dest, orig);
        }
        return dest;
    }

    /**
     * 获取集合的第一个元素，没有时返回NULL
     *
     * @param <T>  泛型
     * @param list 集合
     * @return T
     */
    public static <T> T first(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 获取数组的第一个元素，没有时返回NULL
     *
     * @param <T>   泛型
     * @param array 数组
     * @return T
     */
    public static <T> T first(T... array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[0];
    }

    /**
     * 获取集合的末尾元素，没有时返回NULL
     *
     * @param <T>  泛型
     * @param list 集合
     * @return T
     */
    public static <T> T last(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * 获取数组的末尾元素，没有时返回NULL
     *
     * @param <T>   泛型
     * @param array 数组
     * @return T
     */
    public static <T> T last(T... array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[array.length - 1];
    }

    public static String guid() {
        return GUIDKeyGenerator.getInstance().getGUID();
    }

    public static <T> T[] reverse(T[] array) {
        T temp;
        for (int i = 0; i < array.length / 2; i++) {
            temp = array[i];
            array[i] = array[array.length - 1 - i];
            array[array.length - 1 - i] = temp;
        }
        return array;
    }

    public static <T> List<T> reverse(List<T> array) {
        Collections.reverse(array);
        return array;
    }

    public static List<String> analyze(String text) {
        List<String> list = new ArrayList<String>();
        TokenStream tokenStream = BuguIndex.getInstance().getAnalyzer().tokenStream("*", new StringReader(text));
        CharTermAttribute termAtt = tokenStream.getAttribute(CharTermAttribute.class);
        try {
            while (tokenStream.incrementToken()) {
                list.add(termAtt.toString());
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return list;
    }

    public static class CustomSortOrderComparator implements Comparator<Object>, Serializable {

        private String[] customSort;
        private String idFieldName;

        public CustomSortOrderComparator(String[] customSort, String idFieldName) {
            this.customSort = Arrays.copyOf(customSort, customSort.length);
            this.idFieldName = idFieldName;
        }

        public int compare(Object o1, Object o2) {
            int o1IdKey = ObjectUtil.indexOf(customSort, OgnlUtil.getInstance().getValue(idFieldName, o1).toString());
            int o2IdKey = ObjectUtil.indexOf(customSort, OgnlUtil.getInstance().getValue(idFieldName, o2).toString());
            if (o1IdKey == -1 || o2IdKey == -1) {
                return (o1IdKey == o2IdKey) ? 0 : (o2IdKey == -1 ? -1 : 1);
            }
            return (o1IdKey - o2IdKey) > 0 ? 1 : -1;
        }
    }

}