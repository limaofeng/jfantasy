package com.fantasy.framework.dao.mybatis.interceptors;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.persistence.GeneratedValue;

import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.springframework.beans.factory.InitializingBean;

import com.fantasy.framework.dao.mybatis.keygen.GUIDKeyGenerator;
import com.fantasy.framework.dao.mybatis.keygen.MultiKeyGenerator;
import com.fantasy.framework.dao.mybatis.keygen.SequenceKeyGenerator;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;

/**
 * 注解序列拦截器(扩展mybatis注解主键生成)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:13:36
 * @version 1.0
 */
@Intercepts( { @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class AutoKeyInterceptor implements Interceptor, InitializingBean {

	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;

	private Map<String, KeyGenerator> keyGenerators = null;

	public AutoKeyInterceptor() {
		this.afterPropertiesSet();
	}

	public void afterPropertiesSet() {
		if (ObjectUtil.isNull(keyGenerators))
			keyGenerators = new HashMap<String, KeyGenerator>();
		keyGenerators.put("system-uuid", GUIDKeyGenerator.getInstance());
		keyGenerators.put("fantasy-sequence", new SequenceKeyGenerator());
	}

	public Object intercept(Invocation invocation) throws Throwable {
		Object[] queryArgs = invocation.getArgs();
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		if (SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {
			bindKeyGenerator(ms);
		}
		return invocation.proceed();
	}

	private void bindKeyGenerator(MappedStatement ms) {
		String[] keyProperties = ms.getKeyProperties();
		if ((ObjectUtil.isNull(keyProperties)) && (ObjectUtil.isNotNull(ms.getParameterMap().getType()))) {
			Field[] fields = ClassUtil.getDeclaredFields(ms.getParameterMap().getType(), GeneratedValue.class);
			List<String> keyPropertieNameList = new ArrayList<String>();
			Map<String, KeyGenerator> targetKeyGenerators = new HashMap<String, KeyGenerator>();
			for (Field field : fields) {
				targetKeyGenerators.put(field.getName(), this.keyGenerators.get(((GeneratedValue) ClassUtil.getFieldGenericType(field, GeneratedValue.class)).generator()));
				if (ObjectUtil.isNull(field.getName()))
					targetKeyGenerators.remove(field.getName());
				else {
					keyPropertieNameList.add(field.getName());
				}
			}
			if (!targetKeyGenerators.isEmpty()) {
				ClassUtil.setValue(ms, "keyProperties", keyPropertieNameList.toArray(new String[fields.length]));
				ClassUtil.setValue(ms, "keyGenerator", new MultiKeyGenerator(targetKeyGenerators));
			}
		}
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
	}

	public void setKeyGenerators(Map<String, KeyGenerator> keyGenerators) {
		this.keyGenerators = keyGenerators;
	}

}