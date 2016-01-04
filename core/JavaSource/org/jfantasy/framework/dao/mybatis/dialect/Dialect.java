package org.jfantasy.framework.dao.mybatis.dialect;

/**
 * MyBatis 方言接口
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2012-10-28 下午08:22:02
 */
public abstract interface Dialect {

    public abstract String getLimitString(String sql, int offset, int limit);

    public abstract String getCountString(String sql);

}
