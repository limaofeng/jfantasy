package com.fantasy.framework.util.reflect;

public class Functor {
	private Object source;
	private MethodProxy method;

	private Functor(Object object, MethodProxy method) {
		this.source = object;
		this.method = method;
	}

	public Object call() {
		try {
			return this.method.invoke(this.source);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object call(Object object) {
		return this.method.invoke(this.source, new Object[] { object });
	}

	public static Functor create(Object object, MethodProxy method) {
		return new Functor(object, method);
	}
}