package com.fantasy.framework.lucene.mapper;

public class MapperUtil {

    public static String getEntityName(Class<?> clazz) {
        return clazz.getName().toLowerCase();
    }

}