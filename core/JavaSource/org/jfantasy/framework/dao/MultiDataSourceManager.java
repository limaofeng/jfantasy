package org.jfantasy.framework.dao;

import org.jfantasy.framework.dao.annotations.DataSource;
import org.jfantasy.framework.util.Stack;
import org.jfantasy.framework.util.common.ObjectUtil;

public class MultiDataSourceManager {

    private static ThreadLocal<MultiDataSourceManager> threadLocal = new ThreadLocal<MultiDataSourceManager>();

    private Stack<DataSource> stack = new Stack<DataSource>();

    public static MultiDataSourceManager getManager() {
        MultiDataSourceManager localMessage = threadLocal.get();
        if (ObjectUtil.isNull(localMessage)) {
            threadLocal.set(new MultiDataSourceManager());
        }
        return threadLocal.get();
    }

    public void push(DataSource dataSource) {
        this.stack.push(dataSource);
    }

    public DataSource peek() {
        return this.stack.peek();
    }

    public DataSource pop() {
        return this.stack.pop();
    }

}
