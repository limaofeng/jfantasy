package org.jfantasy.framework.util.reflect;

public class ClassFactory {
    private ClassFactory() {
    }

    public static IClassFactory getFastClassFactory() {
        return new FastClassFactory();
    }

}
