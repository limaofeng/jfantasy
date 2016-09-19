package org.jfantasy.framework.dao.hibernate;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.SQLQuery;
import org.hibernate.property.access.spi.Setter;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;
import org.jfantasy.framework.dao.hibernate.util.TypeFactory;
import org.jfantasy.framework.util.common.ClassUtil;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * 返回结果集转换器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-12 上午9:52:00
 */
@Deprecated
public class AliasToBeanResultTransformer implements ResultTransformer {
    private static final long serialVersionUID = -5199190581393587893L;

    private final static Log LOGGER = LogFactory.getLog(AliasToBeanResultTransformer.class);

    private final Class<?> resultClass;
    private Setter[] setters;
//    private PropertyAccess propertyAccessor;

    private Map<String, String> propertyNames = new HashMap<>();
    private Map<String, Type> propertyTypes = new HashMap<>();

    public AliasToBeanResultTransformer(Class<?> resultClass) {
        if (resultClass == null){
            throw new IllegalArgumentException("resultClass cannot be null");
        }
        this.resultClass = resultClass;
//        this.propertyAccessor = new ChainedPropertyAccessor(new PropertyAccessor[]{PropertyAccessorFactory.getPropertyAccessor(resultClass, null), PropertyAccessorFactory.getPropertyAccessor("field")});

        Field[] fields = ClassUtil.getDeclaredFields(resultClass, Column.class);
        for (Field field : fields) {
            Column column = ClassUtil.getFieldGenericType(field, Column.class);
            propertyNames.put(column.name(), field.getName());
            Type type = getHibernateType(field.getType());
            if (type != null){
                propertyTypes.put(column.name(), type);
            }
        }
    }

    public Type getHibernateType(Class<?> type) {
        return TypeFactory.basic(type.getName());
    }

    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;
        try {
            if (setters == null) {
                setters = new Setter[aliases.length];
                for (int i = 0; i < aliases.length; i++) {
                    String alias = convertColumnToProperty(aliases[i]);
                    if (alias != null) {
                        try {
//                            setters[i] = propertyAccessor.getSetter(resultClass, alias);
                        } catch (PropertyNotFoundException e) {
                            LOGGER.error(e.getMessage(), e);
                        }
                    }
                }
            }
            result = resultClass.newInstance();
            for (int i = 0; i < aliases.length; i++) {
                if (setters[i] != null) {
                    Field field = ClassUtil.getDeclaredField(this.resultClass, convertColumnToProperty(aliases[i]));
                    if (tuple[i] == null) {
                        continue;
                    }
                    setters[i].set(result, ConvertUtils.convert(tuple[i], field.getType()), null);
                }
            }
        } catch (InstantiationException e) {
            LOGGER.error(e.getMessage(),e);
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        } catch (IllegalAccessException e) {
            LOGGER.error(e.getMessage(),e);
            throw new HibernateException("Could not instantiate resultclass: " + resultClass.getName());
        }
        return result;
    }

    public String convertColumnToProperty(String columnName) {
        if (this.propertyNames.containsKey(columnName)) {
            return propertyNames.get(columnName);
        }
        columnName = columnName.toLowerCase();
        StringBuilder buff = new StringBuilder(columnName.length());
        StringTokenizer st = new StringTokenizer(columnName, "_");
        while (st.hasMoreTokens()) {
            buff.append(StringUtils.capitalize(st.nextToken()));
        }
        buff.setCharAt(0, Character.toLowerCase(buff.charAt(0)));
        return buff.toString();
    }

    @SuppressWarnings("rawtypes")
    public List transformList(List collection) {
        return collection;
    }

    protected void custom(SQLQuery query) {
        for (Map.Entry<String, Type> entry : propertyTypes.entrySet()){
            query.addScalar(entry.getKey(), entry.getValue());
        }
    }

}