package org.jfantasy.framework.dao.mybatis.keygen;

import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.log4j.Logger;

import java.sql.Statement;
import java.util.Map;

/**
 * 将多个KeyGenerator对象封装为一个
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-14 下午02:09:05
 */
public class MultiKeyGenerator implements KeyGenerator {

    private static final Logger LOG = Logger.getLogger(MultiKeyGenerator.class);

    private Map<String, KeyGenerator> targetKeyGenerators;

    public MultiKeyGenerator(Map<String, KeyGenerator> targetKeyGenerators) {
        this.targetKeyGenerators = targetKeyGenerators;
    }

    public void processBefore(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
        for (String keyPropertie : paramMappedStatement.getKeyProperties()) {
            try {
                Object value = Ognl.getValue(keyPropertie, paramObject);
                if ((ObjectUtil.isNull(value)) || (StringUtil.isBlank(value))){
                    this.targetKeyGenerators.get(keyPropertie).processBefore(paramExecutor, paramMappedStatement, paramStatement, paramObject);
                }
            } catch (OgnlException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    public void processAfter(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
    }
}