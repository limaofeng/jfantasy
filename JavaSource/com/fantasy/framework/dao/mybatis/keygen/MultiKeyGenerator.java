package com.fantasy.framework.dao.mybatis.keygen;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import java.sql.Statement;
import java.util.Map;
import ognl.Ognl;
import ognl.OgnlException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

/**
 * 将多个KeyGenerator对象封装为一个
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:09:05
 * @version 1.0
 */
public class MultiKeyGenerator implements KeyGenerator {
	private Map<String, KeyGenerator> targetKeyGenerators;

	public MultiKeyGenerator(Map<String, KeyGenerator> targetKeyGenerators) {
		this.targetKeyGenerators = targetKeyGenerators;
	}

	public void processBefore(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
		for (String keyPropertie : paramMappedStatement.getKeyProperties())
			try {
				Object value = Ognl.getValue(keyPropertie, paramObject);
				if ((ObjectUtil.isNull(value)) || (StringUtil.isBlank(value)))
					((KeyGenerator) this.targetKeyGenerators.get(keyPropertie)).processBefore(paramExecutor, paramMappedStatement, paramStatement, paramObject);
			} catch (OgnlException e) {
				e.printStackTrace();
			}
	}

	public void processAfter(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
	}
}