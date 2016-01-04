package org.jfantasy.framework.dao.hibernate.util;

import org.hibernate.type.TypeResolver;
import org.hibernate.type.Type;

public class TypeFactory {

    private TypeFactory(){

    }
    private static final TypeResolver typeResolver =new TypeResolver();

    public static Type basic(String name) {
        return typeResolver.basic(name);
    }

}
