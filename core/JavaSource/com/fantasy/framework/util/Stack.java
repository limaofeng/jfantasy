package com.fantasy.framework.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 类名: Stack 描述: (先进后出) 作者: 李茂峰 创建时间: 2010-2-2
 */
public class Stack<T> {
	private LinkedList<T> list;

	public Stack() {
		list = new LinkedList<T>();
	}

	/**
	 * 从栈中取出元素
	 */
	public T pop() {
		return list.poll();
	}

	/**
	 * 读取对象，但不取出
	 * 
	 * @return
	 */
	public T peek() {
		return list.peek();
	}

	/**
	 * 向栈添加元素
	 * 
	 * @param o
	 */
	public void push(T o) {
		list.addFirst(o);
	}

	/**
	 * 判断栈 是否为空
	 * 
	 * @return boolean
	 */
	public boolean empty() {
		return list.isEmpty();
	}

	/**
	 * 清空栈
	 */
	public void clear() {
		list.clear();
	}

	public List<T> toList() {
		return this.list;
	}

	public int size() {
		return this.list.size();
	}
}
