package com.fantasy.attr;

import com.fantasy.attr.bean.Attribute;
import com.fantasy.framework.util.Stack;
import com.fantasy.framework.util.common.ObjectUtil;

import java.util.List;

/**
 * 用于动态属性上下文
 */
public class DynaBeanQueryManager {
    /**
     * 动态属性
     */
    public List<Attribute> attributes;

    private static ThreadLocal<DynaBeanQueryManager> threadLocal = new ThreadLocal<DynaBeanQueryManager>();

    private Stack<DynaBeanQuery> stack = new Stack<DynaBeanQuery>();
    private final static DynaBeanQuery defaultDynaBeanQuery = new DynaBeanQuery() {
        @Override
        public void addColumn(String name, Class<?> type) {
            throw new RuntimeException("默认动态查询，不能添加字段");
        }
    };

    public static DynaBeanQueryManager getManager() {
        DynaBeanQueryManager localMessage = threadLocal.get();
        if (ObjectUtil.isNull(localMessage)) {
            threadLocal.set(new DynaBeanQueryManager());
        }
        return threadLocal.get();
    }

    public void push(DynaBeanQuery dataSource) {
        this.stack.push(dataSource);
    }

    public DynaBeanQuery peek() {
        DynaBeanQuery dynaBeanQuery = this.stack.peek();
        if (dynaBeanQuery == null) {
            return defaultDynaBeanQuery;
        }
        return dynaBeanQuery;
    }

    public DynaBeanQuery pop() {
        return this.stack.pop();
    }

}
