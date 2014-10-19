package com.fantasy.framework.dao;

import com.fantasy.framework.dao.annotations.DataSource;
import com.fantasy.framework.util.Stack;
import com.fantasy.framework.util.common.ObjectUtil;

public class MultiDataSourceManager {
	
	private static ThreadLocal<MultiDataSourceManager> threadLocal = new ThreadLocal<MultiDataSourceManager>();

	private Stack<DataSource> stack = new Stack<DataSource>();

	public static MultiDataSourceManager getManager() {
		MultiDataSourceManager localMessage = (MultiDataSourceManager) threadLocal.get();
		if (ObjectUtil.isNull(localMessage)) {
			threadLocal.set(new MultiDataSourceManager());
		}
		return (MultiDataSourceManager) threadLocal.get();
	}

	public void push(DataSource dataSource) {
		this.stack.push(dataSource);
	}

	public DataSource peek() {
		return (DataSource) this.stack.peek();
	}

	public DataSource pop() {
		return (DataSource) this.stack.pop();
	}
	
}
