package com.fantasy.framework.util.reflect;

public class Property {
	private String name;
	private MethodProxy readMethodProxy;
	private MethodProxy writeMethodProxy;
	private Class<?> propertyType;
	private boolean write;
	private boolean read;

	Property(String name, MethodProxy readMethodProxy, MethodProxy writeMethodProxy, Class<?> propertyType) {
		this.read = (readMethodProxy != null);
		this.write = (writeMethodProxy != null);
		this.name = name;
		this.readMethodProxy = readMethodProxy;
		this.writeMethodProxy = writeMethodProxy;
		this.propertyType = propertyType;
	}

	public boolean isWrite() {
		return this.write;
	}

	public boolean isRead() {
		return this.read;
	}

	public Object getValue(Object target) {
		return this.readMethodProxy.invoke(target);
	}

	public void setValue(Object target, Object value) {
		this.writeMethodProxy.invoke(target, value);
	}

	public <T> Class<T> getPropertyType() {
		return (Class<T>)this.propertyType;
	}

	public String getName() {
		return this.name;
	}

	public MethodProxy getReadMethod() {
		return this.readMethodProxy;
	}

	public MethodProxy getWriteMethod() {
		return this.writeMethodProxy;
	}
}
