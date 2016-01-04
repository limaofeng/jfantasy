package org.jfantasy.framework.dao.mybatis.binding;

import org.jfantasy.framework.dao.mybatis.proxy.MyBatisMapperProxy;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * MyBatis Mapper 登记处
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2012-10-28 下午08:30:16
 */
public class MyBatisMapperRegistry extends MapperRegistry {

    public MyBatisMapperRegistry(Configuration config) {
        super(config);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        if (!hasMapper(type)) {
            throw new BindingException("Type " + type + " is not known to the MapperRegistry.");
        }
        try {
            return MyBatisMapperProxy.newMapperProxy(type, sqlSession);
        } catch (Exception e) {
            throw new BindingException("Error getting mapper instance. Cause: " + e, e);
        }

    }
}