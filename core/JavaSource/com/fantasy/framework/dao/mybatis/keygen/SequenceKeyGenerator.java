package com.fantasy.framework.dao.mybatis.keygen;

import java.sql.Statement;

import ognl.Ognl;
import ognl.OgnlException;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;

import com.fantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.ObjectUtil;

/**
 * 序列生成器
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:08:52
 * @version 1.0
 */
public class SequenceKeyGenerator implements KeyGenerator {

	private DataBaseKeyGenerator dataBaseKeyGenerator;

	public void processBefore(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
		String[] keyProperties = paramMappedStatement.getKeyProperties();
		if (keyProperties.length == 1)
			try {
				Ognl.setValue(keyProperties[0], paramObject, Long.valueOf(getKeyGenerator().nextValue(paramObject.getClass().getName())));
			} catch (OgnlException e) {
				e.printStackTrace();
			}
	}

	private DataBaseKeyGenerator getKeyGenerator() {
		if (ObjectUtil.isNull(this.dataBaseKeyGenerator)) {
			this.dataBaseKeyGenerator = SpringContextUtil.getBeanByType(DataBaseKeyGenerator.class);
		}
		return this.dataBaseKeyGenerator;
	}

	public void processAfter(Executor paramExecutor, MappedStatement paramMappedStatement, Statement paramStatement, Object paramObject) {
	}

	public void setDataBaseKeyGenerator(DataBaseKeyGenerator dataBaseKeyGenerator) {
		this.dataBaseKeyGenerator = dataBaseKeyGenerator;
	}

}