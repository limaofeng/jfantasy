package com.fantasy.framework.util.ognl;

import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.ognl.typeConverter.DateConverter;
import ognl.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OgnlUtil {

    private final static Log logger = LogFactory.getLog(OgnlUtil.class);

    private static ConcurrentHashMap<String, OgnlUtil> ognlUtilCache = new ConcurrentHashMap<String, OgnlUtil>();
    private ConcurrentHashMap<String, Object> expressions = new ConcurrentHashMap<String, Object>();
    private final ConcurrentHashMap<Class<?>, BeanInfo> beanInfoCache = new ConcurrentHashMap<Class<?>, BeanInfo>();

    private Map<Class<?>, TypeConverter> typeConverters = new HashMap<Class<?>, TypeConverter>();

    private TypeConverter defaultTypeConverter = new DefaultTypeConverter() {

        @SuppressWarnings("rawtypes")
        public Object convertValue(Map context, Object root, Member member, String name, Object value, Class toType) {
            if (OgnlUtil.this.typeConverters.containsKey(toType)) {
                return OgnlUtil.this.typeConverters.get(toType).convertValue(context, root, member, name, value, toType);
            } else if (value != null && OgnlUtil.this.typeConverters.containsKey(ClassUtil.getRealClass(value))) {
                return OgnlUtil.this.typeConverters.get(ClassUtil.getRealClass(value)).convertValue(context, root, member, name, value, toType);
            } else if ("EMPTY".equals(value) && !ClassUtil.isPrimitiveOrWrapperOrStringOrDate(toType)) {
                return ClassUtil.newInstance(toType);
            }
            return super.convertValue(context, root, member, name, value, toType);
        }

    };

    public static boolean containsKey(String key) {
        return ognlUtilCache.containsKey(key);
    }

    public static OgnlUtil getInstance() {
        return getInstance("default");
    }

    public synchronized static OgnlUtil getInstance(String key) {
        OgnlUtil ognlUtil;
        if (!ognlUtilCache.containsKey(key)) {
            if (SpringContextUtil.getApplicationContext() != null) {
                ognlUtil = SpringContextUtil.getBeanByType(OgnlUtil.class);
                if (ognlUtil == null) {
                    ognlUtil = new OgnlUtil();
                    ognlUtil.addTypeConverter(Date.class, new DateConverter());
                }
            } else {
                ognlUtil = new OgnlUtil();
                ognlUtil.addTypeConverter(Date.class, new DateConverter());
            }
            ognlUtilCache.put(key, ognlUtil);
        } else {
            ognlUtil = ognlUtilCache.get(key);
        }
        return ognlUtil;
    }

    public void addTypeConverter(Class<?> type, TypeConverter typeConverter) {
        this.typeConverters.put(type, typeConverter);
    }

    public void setTypeConverters(Map<Class<?>, TypeConverter> typeConverters) {
        this.typeConverters = typeConverters;
    }

    public void setValue(String name, Object root, Object value) {
        try {
            Map<String, Object> context = createDefaultContext(root);
            setValue(name, context, root, value);
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void setValue(String name, Map<String, Object> context, Object root, Object value) throws OgnlException {
        if (name.contains(".")) {
            String[] ns = name.split("\\.");
            String _q = "";
            for (int i = 0; i < ns.length - 1; i++) {
                Object v = getValue(_q + ns[i], root);
                if (v == null) {
                    logger.debug("p:" + _q + ns[i]);
                    Ognl.setValue(compile(_q + ns[i]), context, root, "EMPTY");
                }
                _q = ns[i] + ".";
            }
        }
        Ognl.setValue(compile(name), context, root, value);
    }

    public Object getValue(String key, Object root) {
        return getValue(key, createDefaultContext(root), root);
    }

    public Object getValue(String name, Map<String, Object> context, Object root) {
        try {
            if (context == null) {
                return Ognl.getValue(name, root);
            }
            return Ognl.getValue(compile(name), context, root);
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }
    }

    public <T> T getValue(String name, Object root, Class<T> resultType) {
        return getValue(name, createDefaultContext(root), root, resultType);
    }

    @SuppressWarnings("unchecked")
    public <T> T getValue(String name, Map<String, Object> context, Object root, Class<T> resultType) {
        try {
            return (T) Ognl.getValue(compile(name), context, root, resultType);
        } catch (OgnlException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public Object compile(String expression) throws OgnlException {
        Object o = this.expressions.get(expression);
        if (o == null) {
            this.expressions.putIfAbsent(expression, o = Ognl.parseExpression(expression));
        }
        return o;
    }

    public PropertyDescriptor[] getPropertyDescriptors(Object source) throws IntrospectionException {
        BeanInfo beanInfo = getBeanInfo(source);
        return beanInfo.getPropertyDescriptors();
    }

    public PropertyDescriptor[] getPropertyDescriptors(Class<?> clazz) throws IntrospectionException {
        BeanInfo beanInfo = getBeanInfo(clazz);
        return beanInfo.getPropertyDescriptors();
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBeanMap(Object source, String... excludeProperties) throws IntrospectionException, OgnlException {
        Map<String, Object> beanMap = new HashMap<String, Object>();
        Map<String, ?> sourceMap = Ognl.createDefaultContext(source);
        PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(source);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String propertyName = propertyDescriptor.getDisplayName();
            if (ObjectUtil.indexOf(excludeProperties, propertyName) != -1) {
                continue;
            }
            Method readMethod = propertyDescriptor.getReadMethod();
            if (readMethod != null) {
                Object expr = compile(propertyName);
                Object value = Ognl.getValue(expr, sourceMap, source);
                beanMap.put(propertyName, value);
            } else {
                beanMap.put(propertyName, "There is no read method for " + propertyName);
            }
        }
        return beanMap;
    }

    public BeanInfo getBeanInfo(Object from) throws IntrospectionException {
        return getBeanInfo(ClassUtil.getRealClass(from));
    }

    public BeanInfo getBeanInfo(Class<?> clazz) throws IntrospectionException {
        if (!this.beanInfoCache.containsKey(clazz)) {
            this.beanInfoCache.putIfAbsent(clazz, Introspector.getBeanInfo(clazz, Object.class));
        }
        return this.beanInfoCache.get(clazz);
    }

    public void setProperties(Map<String, ?> props, Object o, Map<String, Object> context, boolean throwPropertyExceptions) throws Exception {
        if (props == null) {
            return;
        }
        Ognl.setTypeConverter(context, getTypeConverterFromContext());
        Object oldRoot = Ognl.getRoot(context);
        Ognl.setRoot(context, o);
        for (Map.Entry<String, ?> entry : props.entrySet()) {
            String expression = entry.getKey();
            internalSetProperty(expression, entry.getValue(), o, context, throwPropertyExceptions);
        }
        Ognl.setRoot(context, oldRoot);
    }

    void internalSetProperty(String name, Object value, Object o, Map<String, Object> context, boolean throwPropertyExceptions) throws Exception {
        try {
            setValue(name, context, o, value);
        } catch (OgnlException e) {
            Throwable reason = e.getReason();
            String msg = "Caught OgnlException while setting property '" + name + "' on type '" + o.getClass().getName() + "'.";
            Throwable exception = reason == null ? e : reason;
            if (throwPropertyExceptions)
                throw new Exception(msg, exception);
        }
    }

    public TypeConverter getTypeConverterFromContext() {
        return this.defaultTypeConverter;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> createDefaultContext(Object target) {
        OgnlContext ognlContext = (OgnlContext) Ognl.createDefaultContext(target, new ClassResolver() {

            private DefaultClassResolver resolver = new DefaultClassResolver();

            @Override
            public Class classForName(String className, Map context) throws ClassNotFoundException {
                logger.debug(className);
                return resolver.classForName(className, context);
            }

        }, this.defaultTypeConverter);
        return (Map<String, Object>) ognlContext;
    }

    public void copy(Object from, Object to) {
        copy(from, to, null, null);
    }

    @SuppressWarnings("rawtypes")
    public void copy(Object from, Object to, Collection<String> exclusions, Collection<String> inclusions) {
        if ((from == null) || (to == null)) {
            return;
        }
        TypeConverter conv = getTypeConverterFromContext();
        Map contextFrom = Ognl.createDefaultContext(from);
        Ognl.setTypeConverter(contextFrom, conv);
        Map contextTo = Ognl.createDefaultContext(to);
        Ognl.setTypeConverter(contextTo, conv);
        PropertyDescriptor[] fromPds;
        PropertyDescriptor[] toPds;
        try {
            fromPds = getPropertyDescriptors(from);
            toPds = getPropertyDescriptors(to);
        } catch (IntrospectionException e) {
            logger.error("An error occured", e);
            return;
        }
        Map<String, PropertyDescriptor> toPdHash = new HashMap<String, PropertyDescriptor>();
        for (PropertyDescriptor toPd : toPds) {
            toPdHash.put(toPd.getName(), toPd);
        }
        for (PropertyDescriptor fromPd : fromPds) {
            if (fromPd.getReadMethod() != null) {
                boolean copy = true;
                if ((exclusions != null) && (exclusions.contains(fromPd.getName())))
                    copy = false;
                else if ((inclusions != null) && (!inclusions.contains(fromPd.getName()))) {
                    copy = false;
                }
                if (copy) {
                    PropertyDescriptor toPd = toPdHash.get(fromPd.getName());
                    if ((toPd == null) || (toPd.getWriteMethod() == null))
                        continue;
                    try {
                        Object expr = compile(fromPd.getName());
                        Object value = Ognl.getValue(expr, contextFrom, from);
                        Ognl.setValue(expr, contextTo, to, value);
                    } catch (OgnlException e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        }
    }
}