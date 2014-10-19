package com.fantasy.framework.lucene.backend;

import java.lang.reflect.Field;

import com.fantasy.framework.lucene.annotations.Compare;
import com.fantasy.framework.lucene.mapper.DataType;
import com.fantasy.framework.lucene.mapper.FieldUtil;

public class CompareChecker {
	private Object obj;

	public CompareChecker(Object obj) {
		this.obj = obj;
	}

	public boolean isFit(Field f, Compare compare, String value) {
		boolean fit = false;
		switch (compare.ordinal()+1) {
		case 1:
			fit = isEquals(f, value);
			break;
		case 2:
			fit = notEquals(f, value);
			break;
		case 3:
			fit = greaterThan(f, value);
			break;
		case 4:
			fit = greaterThanEquals(f, value);
			break;
		case 5:
			fit = lessThan(f, value);
			break;
		case 6:
			fit = lessThanEquals(f, value);
			break;
		case 7:
			fit = isNull(FieldUtil.get(this.obj, f));
			break;
		case 8:
			fit = notNull(FieldUtil.get(this.obj, f));
			break;
		}
		return fit;
	}

	private boolean isEquals(Field f, String value) {
		Object objValue = FieldUtil.get(this.obj, f);
		if (objValue == null) {
			return false;
		}
		String objStr = objValue.toString();
		Class<?> type = f.getType();
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
		if ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) {
			return Double.parseDouble(objStr) == Double.parseDouble(value);
		}

		return false;
	}

	private boolean notEquals(Field f, String value) {
		Object objValue = FieldUtil.get(this.obj, f);
		if (objValue == null) {
			return false;
		}
		return !isEquals(f, value);
	}

	private boolean greaterThan(Field f, String value) {
		Object objValue = FieldUtil.get(this.obj, f);
		if (objValue == null) {
			return false;
		}
		String objStr = objValue.toString();
		Class<?> type = f.getType();
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
		if ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) {
			return Double.parseDouble(objStr) > Double.parseDouble(value);
		}

		return false;
	}

	private boolean greaterThanEquals(Field f, String value) {
		Object objValue = FieldUtil.get(this.obj, f);
		if (objValue == null) {
			return false;
		}
		String objStr = objValue.toString();
		Class<?> type = f.getType();
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
		if ((DataType.isDouble(type)) || (DataType.isDoubleObject(type))) {
			return Double.parseDouble(objStr) >= Double.parseDouble(value);
		}

		return false;
	}

	private boolean lessThan(Field f, String value) {
		Object objValue = FieldUtil.get(this.obj, f);
		if (objValue == null) {
			return false;
		}
		return !greaterThanEquals(f, value);
	}

	private boolean lessThanEquals(Field f, String value) {
		Object objValue = FieldUtil.get(this.obj, f);
		if (objValue == null) {
			return false;
		}
		return !greaterThan(f, value);
	}

	private boolean isNull(Object objValue) {
		return objValue == null;
	}

	private boolean notNull(Object objValue) {
		return !isNull(objValue);
	}
}
