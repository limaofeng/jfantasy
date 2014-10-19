package com.fantasy.file.interceptor;

import com.fantasy.file.bean.FileDetail;
import com.fantasy.framework.util.Stack;

public class FileContext {

	private static ThreadLocal<FileContext> threadLocal = new ThreadLocal<FileContext>();

	private Stack<FileDetail> stack = new Stack<FileDetail>();

	public synchronized static FileContext getContext() {
		FileContext fileContext = (FileContext) threadLocal.get();
		if (fileContext == null) {
			threadLocal.set(fileContext = new FileContext());
		}
		return fileContext;
	}

	/**
	 * 从栈中取出元素
	 */
	public FileDetail pop() {
		return stack.pop();
	}

	/**
	 * 读取对象，但不取出
	 * 
	 * @return
	 */
	public FileDetail peek() {
		return stack.peek();
	}

	/**
	 * 向栈添加元素
	 * 
	 * @param o
	 */
	public void push(FileDetail o) {
		stack.push(o);
	}

    public void clear(){
        stack.clear();
    }

	public boolean empty() {
		return stack.empty();
	}

}
