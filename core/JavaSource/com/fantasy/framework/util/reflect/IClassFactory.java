package com.fantasy.framework.util.reflect;

public abstract interface IClassFactory {

    public abstract <T> IClass<T> getClass(Class<T> paramClass);

}