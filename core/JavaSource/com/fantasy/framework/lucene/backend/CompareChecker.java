package com.fantasy.framework.lucene.backend;

import com.fantasy.framework.lucene.annotations.Compare;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.util.reflect.Property;

public class CompareChecker {
    private Object obj;

    public CompareChecker(Object obj) {
        this.obj = obj;
    }

    public boolean isFit(Property p, Compare compare, String value) {
        boolean fit = false;
        switch (compare.ordinal() + 1) {
            case 1:
                fit = isEquals(p, value);
                break;
            case 2:
                fit = notEquals(p, value);
                break;
            case 3:
                fit = greaterThan(p, value);
                break;
            case 4:
                fit = greaterThanEquals(p, value);
                break;
            case 5:
                fit = lessThan(p, value);
                break;
            case 6:
                fit = lessThanEquals(p, value);
                break;
            case 7:
                fit = isNull(p.getValue(this.obj));
                break;
            case 8:
                fit = notNull(p.getValue(this.obj));
                break;
            default:
        }
        return fit;
    }

    private boolean isEquals(Property p, String value) {
        Object objValue = p.getValue(this.obj);
        if (objValue == null) {
            return false;
        }
        String objStr = objValue.toString();
        Class<?> type = p.getPropertyType();
        if (DataType.isString(type)) {
            return value.equals(objStr);
        }
        if ((DataType.isBoolean(type)) || (DataType.isBooleanObject(type))) {
            return Boolean.parseBoolean(objStr) == Boolean.parseBoolean(value);
        }
        if ((DataType.isChar(type)) || (DataType.isCharObject(type))) {
            return objStr.charAt(0) == value.charAt(0);
        }
        if ((DataType.isInteger(type)) || (DataType.isIntegerObject(type))) {
            return Integer.parseInt(objStr) == Integer.parseInt(value);
        }
        if ((DataType.isLong(type)) || (DataType.isLongObject(type))) {
            return Long.parseLong(objStr) == Long.parseLong(value);
        }
        if ((DataType.isShort(type)) || (DataType.isShortObject(type))) {
            return Short.parseShort(objStr) == Short.parseShort(value);
        }
        if ((DataType.isFloat(type)) || (DataType.isFloatObject(type))) {
            return Float.parseFloat(objStr) == Float.parseFloat(value);
        }
        return ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) && Double.parseDouble(objStr) == Double.parseDouble(value);

    }

    private boolean notEquals(Property p, String value) {
        Object objValue = p.getValue(this.obj);
        return objValue != null && !isEquals(p, value);
    }

    private boolean greaterThan(Property p, String value) {
        Object objValue = p.getValue(this.obj);
        if (objValue == null) {
            return false;
        }
        String objStr = objValue.toString();
        Class<?> type = p.getPropertyType();
        if ((DataType.isInteger(type)) || (DataType.isIntegerObject(type))) {
            return Integer.parseInt(objStr) > Integer.parseInt(value);
        }
        if ((DataType.isLong(type)) || (DataType.isLongObject(type))) {
            return Long.parseLong(objStr) > Long.parseLong(value);
        }
        if ((DataType.isShort(type)) || (DataType.isShortObject(type))) {
            return Short.parseShort(objStr) > Short.parseShort(value);
        }
        if ((DataType.isFloat(type)) || (DataType.isFloatObject(type))) {
            return Float.parseFloat(objStr) > Float.parseFloat(value);
        }
        return ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) && Double.parseDouble(objStr) > Double.parseDouble(value);

    }

    private boolean greaterThanEquals(Property p, String value) {
        Object objValue = p.getValue(p);
        if (objValue == null) {
            return false;
        }
        String objStr = objValue.toString();
        Class<?> type = p.getPropertyType();
        if ((DataType.isInteger(type)) || (DataType.isIntegerObject(type))) {
            return Integer.parseInt(objStr) >= Integer.parseInt(value);
        }
        if ((DataType.isLong(type)) || (DataType.isLongObject(type))) {
            return Long.parseLong(objStr) >= Long.parseLong(value);
        }
        if ((DataType.isShort(type)) || (DataType.isShortObject(type))) {
            return Short.parseShort(objStr) >= Short.parseShort(value);
        }
        if ((DataType.isFloat(type)) || (DataType.isFloatObject(type))) {
            return Float.parseFloat(objStr) >= Float.parseFloat(value);
        }
        return ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) && Double.parseDouble(objStr) >= Double.parseDouble(value);

    }

    private boolean lessThan(Property p, String value) {
        Object objValue = p.getValue(this.obj);
        return objValue != null && !greaterThanEquals(p, value);
    }

    private boolean lessThanEquals(Property p, String value) {
        Object objValue = p.getValue(this.obj);
        return objValue != null && !greaterThan(p, value);
    }

    private boolean isNull(Object objValue) {
        return objValue == null;
    }

    private boolean notNull(Object objValue) {
        return !isNull(objValue);
    }
}
