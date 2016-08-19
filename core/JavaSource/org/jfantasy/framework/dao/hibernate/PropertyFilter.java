package org.jfantasy.framework.dao.hibernate;

import org.jfantasy.framework.dao.hibernate.util.ReflectionUtils;
import org.jfantasy.framework.error.IgnoreException;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

@ApiModel("通用过滤器")
public class PropertyFilter {
    public static final String OR_SEPARATOR = "_OR_";
    @ApiModelProperty("名称")
    private String[] propertyNames;
    @ApiModelProperty("类型")
    private Class<?> propertyType;
    @ApiModelProperty("值")
    private Object propertyValue;
    @ApiModelProperty("过滤类型")
    private MatchType matchType;
    @ApiModelProperty("完整表达式")
    private String filterName;

    public PropertyFilter(String filterName) {
        this.filterName = filterName;
        String matchTypeCode = StringUtils.substringBefore(filterName, "_");
        try {
            this.matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (IgnoreException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
        }
        if (!(MatchType.NULL.equals(this.matchType) || MatchType.NOTNULL.equals(this.matchType) || MatchType.EMPTY.equals(this.matchType) || MatchType.NOTEMPTY.equals(this.matchType))) {
            throw new IgnoreException("没有设置value时,查询条件必须为 is null,not null,empty,not empty");
        }
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        this.propertyNames = propertyNameStr.split(OR_SEPARATOR);
        this.propertyValue = new Object();
    }

    public PropertyFilter(String filterName, Enum<?> value) {
        this.initialize(filterName);
        if (this.propertyType != Enum.class) {
            throw new IgnoreException("查询类型类型必须为枚举类型(E)");
        }
        this.propertyType = value.getClass();
        this.propertyValue = value;
    }

    public PropertyFilter(String filterName, Enum<?>... value) {
        this.initialize(filterName);
        if (this.propertyType != Enum.class) {
            throw new IgnoreException("");
        }
        this.propertyType = Array.get(value, 0).getClass();
        if (!(MatchType.IN.equals(this.matchType) || MatchType.NOTIN.equals(this.matchType))) {
            throw new IgnoreException("有多个条件时,查询条件必须为 in 或者 not in ");
        }
        this.propertyValue = value;
    }

    public PropertyFilter(String filterName, String value) {
        this.initialize(filterName);
        this.setPropertyValue(value);
    }

    private void setPropertyValue(String value) {
        if (PropertyFilter.MatchType.BETWEEN.equals(this.matchType)) {
            Object array = ClassUtil.newInstance(this.propertyType, 2);
            String[] tempArray = StringUtil.tokenizeToStringArray(value, "~");
            for (int i = 0; i < tempArray.length; i++) {
                Array.set(array, i, ReflectionUtils.convertStringToObject(tempArray[i], this.propertyType));
            }
            this.propertyValue = array;
        } else if (this.propertyType == Enum.class) {
            this.propertyValue = value;
        } else {
            this.propertyValue = ReflectionUtils.convertStringToObject(value, this.propertyType);
        }
    }

    public <T> PropertyFilter(String filterName, T... value) {
        this.initialize(filterName);
        if (!(MatchType.IN.equals(this.matchType) || MatchType.NOTIN.equals(this.matchType)) && value.length > 1) {
            throw new IgnoreException("有多个条件时,查询条件必须为 in 或者 not in ");
        }
        if (MatchType.IN.equals(this.matchType) || MatchType.NOTIN.equals(this.matchType)) {
            Object array = this.propertyType.isAssignableFrom(Enum.class) ? new String[value.length] : ClassUtil.newInstance(this.propertyType, Array.getLength(value));
            for (int i = 0; i < Array.getLength(value); i++) {
                Array.set(array, i, this.propertyType == Enum.class ? Array.get(value, i).toString() : ReflectionUtils.convertStringToObject(Array.get(value, i).toString(), this.propertyType));
            }
            this.propertyValue = array;
        } else {
            setPropertyValue(value[0].toString());
        }
    }

    private void initialize(String filterName) {
        this.filterName = filterName;
        String matchTypeStr = StringUtils.substringBefore(filterName, "_");
        String matchTypeCode = StringUtils.substring(matchTypeStr, 0, matchTypeStr.length() - 1);
        String propertyTypeCode = StringUtils.substring(matchTypeStr, matchTypeStr.length() - 1, matchTypeStr.length());
        try {
            this.matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (IgnoreException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性比较类型.", e);
        }
        try {
            this.propertyType = (Enum.valueOf(PropertyType.class, propertyTypeCode)).getValue();
        } catch (IgnoreException e) {
            throw new IllegalArgumentException("filter名称" + filterName + "没有按规则编写,无法得到属性值类型.", e);
        }
        String propertyNameStr = StringUtils.substringAfter(filterName, "_");
        this.propertyNames = propertyNameStr.split(OR_SEPARATOR);

        Assert.isTrue(this.propertyNames.length > 0, "filter名称" + filterName + "没有按规则编写,无法得到属性名称.");
    }

    public boolean isMultiProperty() {
        return this.propertyNames.length > 1;
    }

    public String[] getPropertyNames() {
        return this.propertyNames;
    }

    public String getPropertyName() {
        if (this.propertyNames.length > 1) {
            throw new IllegalArgumentException("There are not only one property");
        }
        return this.propertyNames[0];
    }

    public Object getPropertyValue() {
        return this.propertyValue;
    }

    public <T> T getPropertyValue(T o) {
        return (T) getPropertyValue(o.getClass());
    }

    public <T> T getPropertyValue(Class<T> clazz) {
        if (this.getPropertyType().isAssignableFrom(Enum.class)) {
            AtomicReference<Class> enumClass = new AtomicReference<Class>(clazz.isArray() ? clazz.getComponentType() : clazz);
            if (propertyValue instanceof String) {
                return clazz.cast(Enum.valueOf(enumClass.get(), (String) propertyValue));
            } else if (propertyValue instanceof String[]) {
                Object array = ClassUtil.newInstance(enumClass.get(), Array.getLength(propertyValue));
                for (int i = 0; i < Array.getLength(propertyValue); i++) {
                    Array.set(array, i, Enum.valueOf(enumClass.get(), (String) Array.get(propertyValue, i)));
                }
                return clazz.cast(array);
            }
        }
        return ReflectionUtils.convert(this.getPropertyValue(), clazz);
    }

    public <T> Class<T> getPropertyType() {
        return (Class<T>) this.propertyType;
    }

    public MatchType getMatchType() {
        return this.matchType;
    }

    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    public void setPropertyType(Class<?> propertyType) {
        this.propertyType = propertyType;
    }

    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    public void setMatchType(MatchType matchType) {
        this.matchType = matchType;
    }

    public enum MatchType {
        /**
         * 等于
         */
        EQ(false),
        /**
         * 模糊查询
         */
        LIKE(false),
        /**
         * 小于
         */
        LT(false),
        /**
         * 大于
         */
        GT(false),
        /**
         * 小于等于
         */
        LE(false),
        /**
         * 大于等于
         */
        GE(false),
        /**
         * in
         */
        IN(true),
        /**
         * not in
         */
        NOTIN(true),
        /**
         * 不等于
         */
        NE(false),
        /**
         * is null
         */
        NULL,
        /**
         * not null
         */
        NOTNULL,
        /**
         *
         */
        EMPTY,
        /**
         *
         */
        NOTEMPTY, BETWEEN(false), SQL(false);

        /**
         * 是否存在参数
         */
        private boolean none;
        /**
         * 是否有多个参数
         */
        private boolean multi;

        MatchType() {
            this(true, false);
        }

        MatchType(boolean multi) {
            this(false, multi);
        }

        MatchType(boolean none, boolean multi) {
            this.none = none;
            this.multi = multi;
        }

        public static MatchType get(String str) {
            for (MatchType matchType : MatchType.values()) {
                if (RegexpUtil.find(str, "^" + matchType.toString())) {
                    return matchType;
                }
            }
            return null;
        }

        public static boolean is(String str) {
            return get(str) != null;
        }

        public boolean isMulti() {
            return multi;
        }

        public boolean isNone() {
            return none;
        }
    }

    public enum PropertyType {
        S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class), M(BigDecimal.class), E(Enum.class);

        private Class clazz;

        PropertyType(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class getValue() {
            return this.clazz;
        }

    }

    public String getFilterName() {
        return filterName;
    }

    @Override
    public String toString() {
        return "PropertyFilter [matchType=" + matchType + ", propertyNames=" + Arrays.toString(propertyNames) + ", propertyType=" + propertyType + ", propertyValue=" + propertyValue + "]";
    }

}