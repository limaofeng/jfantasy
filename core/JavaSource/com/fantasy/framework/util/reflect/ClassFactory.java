package com.fantasy.framework.util.reflect;

public class ClassFactory {
    private ClassFactory() {
    }

    public static IClassFactory getFastClassFactory() {
        return new FastClassFactory();
    }

}
