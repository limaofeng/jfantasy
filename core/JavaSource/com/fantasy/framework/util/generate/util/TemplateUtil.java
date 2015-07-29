package com.fantasy.framework.util.generate.util;

import java.util.Map;
import java.util.regex.Matcher;

import com.fantasy.framework.util.regexp.RegexpUtil;

public class TemplateUtil {
    private Map<String, String> dataTypes;

    public TemplateUtil(Map<String, String> dataTypes) {
        this.dataTypes = dataTypes;
    }

    public String toJavaType(String dataType) {
        return (String) this.dataTypes.get(dataType.toUpperCase());
    }

    public String toJavaName(String columnName) {
        return RegexpUtil.replace(columnName, "_[a-zA-Z]", new RegexpUtil.AbstractReplaceCallBack() {
            public String doReplace(String s, int i, Matcher m) {
                return s.replaceFirst("_", "").toUpperCase();
            }
        });
    }

    public String upperCaseFirst(String propName) {
        return propName.substring(0, 1).toUpperCase().concat(propName.substring(1));
    }

    public String lowerCaseFirst(String propName) {
        return propName.substring(0, 1).toLowerCase().concat(propName.substring(1));
    }

    public String toSqlmapJavaType(String dataType) {
        return toJavaType(dataType);
    }

    public String toSqlmapJdbcType(String dataType) {
        return dataType;
    }

    public String toUpperCase(String str) {
        return str.toUpperCase();
    }

    public String toSimpleJavaType(String javaType) {
        return javaType.split("\\.")[javaType.split("\\.").length - 1];
    }
}