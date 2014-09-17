package com.fantasy.framework.util.asm;

/**
 * 类属性信息
 * 
 * @功能描述 用于生成动态类时，属性的描述信息
 * @author 李茂峰
 * @since 2013-5-31 上午09:56:39
 * @version 1.0
 */
public class Property {
	/**
	 * 属性名称
	 */
	private String name;
	/**
	 * 属性类型
	 */
	private Class<?> type;
	/**
	 * 泛型
	 */
	private Class<?>[] genericTypes = new Class<?>[0];
	/**
	 * 是否可以写入(set操作)
	 */
	private boolean write = true;
	/**
	 * 是否可以读取(get操作)
	 */
	private boolean read = true;

	public Property(String name, Class<?> type) {
		super();
		this.name = name;
		this.type = type;
	}

	public Property(String name, Class<?> type, boolean read, boolean write) {
		super();
		this.name = name;
		this.type = type;
		this.read = read;
		this.write = write;
	}

	public Property(String name, Class<?> type, Class<?>[] genericTypes) {
		super();
		this.name = name;
		this.type = type;
		this.genericTypes = genericTypes;
	}

	public Property(String name, Class<?> type, Class<?>[] genericTypes, boolean read, boolean write) {
		super();
		this.name = name;
		this.type = type;
		this.genericTypes = genericTypes;
		this.read = read;
		this.write = write;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public boolean isWrite() {
		return write;
	}

	public boolean isRead() {
		return read;
	}

	public Class<?>[] getGenericTypes() {
		return genericTypes;
	}
}
