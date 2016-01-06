package org.jfantasy.framework.lucene.mapper;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DataType {
	private DataType(){}
	public static boolean isString(Class<?> type) {
		return type.equals(String.class);
	}

	public static boolean isInteger(Class<?> type) {
		return type.equals(Integer.TYPE);
	}

	public static boolean isIntegerObject(Class<?> type) {
		return type.equals(Integer.class);
	}

	public static boolean isLong(Class<?> type) {
		return type.equals(Long.TYPE);
	}

	public static boolean isLongObject(Class<?> type) {
		return type.equals(Long.class);
	}

	public static boolean isShort(Class<?> type) {
		return type.equals(Short.TYPE);
	}

	public static boolean isShortObject(Class<?> type) {
		return type.equals(Short.class);
	}

	public static boolean isFloat(Class<?> type) {
		return type.equals(Float.TYPE);
	}

	public static boolean isFloatObject(Class<?> type) {
		return type.equals(Float.class);
	}

	public static boolean isDouble(Class<?> type) {
		return type.equals(Double.TYPE);
	}

	public static boolean isDoubleObject(Class<?> type) {
		return type.equals(Double.class);
	}

	public static boolean isBoolean(Class<?> type) {
		return type.equals(Boolean.TYPE);
	}

	public static boolean isBooleanObject(Class<?> type) {
		return type.equals(Boolean.class);
	}

	public static boolean isChar(Class<?> type) {
		return type.equals(Character.TYPE);
	}

	public static boolean isCharObject(Class<?> type) {
		return type.equals(Character.class);
	}

	public static boolean isDate(Class<?> type) {
		return type.equals(Date.class);
	}

	public static boolean isTimestamp(Class<?> type) {
		return type.equals(Timestamp.class);
	}

	public static boolean isList(Class<?> type) {
		return (type.equals(List.class)) || (type.equals(ArrayList.class)) || (type.equals(LinkedList.class));
	}

	public static boolean isSet(Class<?> type) {
		return (type.equals(Set.class)) || (type.equals(HashSet.class)) || (type.equals(TreeSet.class));
	}

	public static boolean isMap(Class<?> type) {
		return (type.equals(Map.class)) || (type.equals(HashMap.class)) || (type.equals(TreeMap.class));
	}
}
