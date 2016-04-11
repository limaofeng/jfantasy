package org.jfantasy.framework.dao.mybatis.interceptors;

import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.util.common.ClassUtil;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Properties;

/**
 * 实体公共属性，自动填充拦截器
 * 
 * @author 李茂峰
 * @since 2012-10-28 下午08:09:13
 * @version 1.0
 */
@Intercepts( { @org.apache.ibatis.plugin.Signature(type = org.apache.ibatis.executor.Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class BusEntityInterceptor implements Interceptor {
	/**
	 * 默认编辑人
	 */
	private String defaultModifier;
	/**
	 * 默认创建人
	 */
	private String defaultCreator;
	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;

	public Object intercept(Invocation invocation) throws Throwable {
		Object[] queryArgs = invocation.getArgs();
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameterObject = invocation.getArgs()[PARAMETER_INDEX];
		if (BaseBusEntity.class.isAssignableFrom(ms.getParameterMap().getType())) {
			if (SqlCommandType.INSERT.equals(ms.getSqlCommandType())) {
				UserDetails userDetails = null;//TODO SpringSecurityUtils.getCurrentUser();
				if (ObjectUtil.isNotNull(userDetails)) {
					ClassUtil.setValue(parameterObject, "creator", userDetails.getUsername());
					ClassUtil.setValue(parameterObject, "modifier", userDetails.getUsername());
				} else {
					ClassUtil.setValue(parameterObject, "creator", this.defaultCreator);
					ClassUtil.setValue(parameterObject, "modifier", this.defaultModifier);
				}
				Date now = DateUtil.now();
				ClassUtil.setValue(parameterObject, "createTime", now);
				ClassUtil.setValue(parameterObject, "modifyTime", now);
			} else if (SqlCommandType.UPDATE.equals(ms.getSqlCommandType())) {
				UserDetails userDetails = null;//TODO SpringSecurityUtils.getCurrentUser();
				if (ObjectUtil.isNotNull(userDetails)){
                    ClassUtil.setValue(parameterObject, "modifier", userDetails.getUsername());
                }else {
                    ClassUtil.setValue(parameterObject, "modifier", this.defaultModifier);
                }
				ClassUtil.setValue(parameterObject, "modifyTime", DateUtil.now());
			}
		}
		return invocation.proceed();
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setProperties(Properties properties) {
		this.defaultModifier = properties.getProperty("modifier", "unknown");
		this.defaultCreator = properties.getProperty("creator", "unknown");
	}
}