package com.fantasy.framework.util.reflect;

public class ClassFactory {

	public static IClassFactory getFastClassFactory() {
		return new FastClassFactory();
	}

}
