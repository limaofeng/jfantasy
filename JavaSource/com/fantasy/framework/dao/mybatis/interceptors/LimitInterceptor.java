package com.fantasy.framework.dao.mybatis.interceptors;

import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.mybatis.dialect.Dialect;
import com.fantasy.framework.util.common.ClassUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.PropertiesHelper;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 扩展翻页实现
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:08:34
 * @version 1.0
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class }) })
public class LimitInterceptor implements Interceptor {

	private static final Logger logger = Logger.getLogger(LimitInterceptor.class);

	static int MAPPED_STATEMENT_INDEX = 0;
	static int PARAMETER_INDEX = 1;
	static int RESULT_HANDLER_INDEX = 3;

	Dialect dialect;

	public Object intercept(Invocation invocation) throws Throwable {
		try {
			Pager<?> pager = getPager(invocation.getArgs()[PARAMETER_INDEX]);
			if (ObjectUtil.isNotNull(pager)) {// 如果参数中有Pager对象，执行翻页逻辑,否则按普通逻辑处理查询
				return processIntercept(invocation, invocation.getArgs());
			}
			return invocation.proceed();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		}
	}

	/**
	 * 
	 * @功能描述
	 * @param invocation
	 * @param queryArgs
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	private Object processIntercept(Invocation invocation, final Object[] queryArgs) throws Throwable {
		MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
		Object parameter = queryArgs[PARAMETER_INDEX];
		Pager<?> pager = getPager(parameter);
		if (pager.getFirst() == 0) {// 如果设置了，数据开始的位置，不通过页面计算开始位置
			pager.setTotalCount(executeForCount(ms, parameter));
		}
		pager.setPageItems(executeForList(invocation, ms, parameter));
		return pager.getPageItems();
	}

	/**
	 * 返回 查询调用总的pager对象
	 * 
	 * @功能描述
	 * @param parameterObject
	 * @return
	 */
	private Pager<?> getPager(Object parameterObject) throws Throwable {
		Pager<?> pager = null;
		if (ObjectUtil.isNotNull(parameterObject) && Map.class.isAssignableFrom(parameterObject.getClass())) {
			Map<String, Object> param = (Map<String, Object>) parameterObject;
			pager = param.containsKey("pager") ? (Pager<?>) param.get("pager") : null;
		}
		return pager;
	}

	/**
	 * 查询结果集的总数据条数
	 * 
	 * @功能描述
	 * @param ms
	 * @param parameter
	 * @return
	 */
	private int executeForCount(MappedStatement ms, Object parameter) {
		int count = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try {
			SqlSource sqlSource = getCountSqlSource(ms, parameter);
			MappedStatement newMappedStatement = copyMappedStatementBySqlSource(ms, sqlSource);
			ClassUtil.setValue(newMappedStatement, "id", newMappedStatement.getId() + "_count");
			connection = newMappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
			ParameterHandler dp = new DefaultParameterHandler(ms, parameter, sqlSource.getBoundSql(parameter));
			statement = connection.prepareStatement(sqlSource.getBoundSql(parameter).getSql());
			dp.setParameters(statement);
			rs = statement.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (statement != null)
					statement.close();
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return count;
	}

	/**
	 * 查询显示页对应的结果集
	 * 
	 * @功能描述
	 * @param invocation
	 * @param ms
	 * @param parameterObject
	 * @return
	 * @throws Throwable
	 */
	private List executeForList(Invocation invocation, MappedStatement ms, Object parameterObject) throws Throwable {
		SqlSource sqlSource = getPageLimitSqlSource(ms, parameterObject);
		MappedStatement newMappedStatement = copyMappedStatementBySqlSource(ms, sqlSource);
		invocation.getArgs()[0] = newMappedStatement;
		return (List) invocation.proceed();
	}

	/**
	 * 获取翻页查询中查询总条数的SqlSource对象
	 * 
	 * @功能描述
	 * @param mappedStatement
	 * @param parameterObject
	 * @return
	 */
	private SqlSource getCountSqlSource(MappedStatement mappedStatement, Object parameterObject) {
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(mappedStatement.getConfiguration());
		String mapperSQL = getMapperSQL(mappedStatement, parameterObject);
		Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
		String newSql = this.dialect.getCountString(mapperSQL);
        return sqlSourceParser.parse(newSql, parameterType, (Map<String, Object>) parameterObject);
	}

	/**
	 * 
	 * @功能描述
	 * @param mappedStatement
	 * @param parameterObject
	 * @return
	 * @throws Throwable
	 */
	private SqlSource getPageLimitSqlSource(MappedStatement mappedStatement, Object parameterObject) throws Throwable {
		SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(mappedStatement.getConfiguration());
		String mapperSQL = getMapperSQL(mappedStatement, parameterObject);
		if(parameterObject == null){
			return sqlSourceParser.parse(mapperSQL,Object.class, (Map<String, Object>) parameterObject);
		}else{
			Class<?> parameterType = parameterObject.getClass();
			Pager<?> pager = getPager(parameterObject);
			int pageSize = (pager.getTotalCount() - pager.getFirst()) < pager.getPageSize() ? (pager.getTotalCount() - pager.getFirst()) : pager.getPageSize();
			String newSql = this.dialect.getLimitString(mapperSQL, pager.getFirst(), pageSize);
            return sqlSourceParser.parse(newSql, parameterType, (Map<String, Object>)parameterObject);
		}
	}

	/**
	 * 获取配置的sql
	 * 
	 * @功能描述
	 * @param mappedStatement
	 * @param parameterObject
	 * @return
	 */
	private String getMapperSQL(MappedStatement mappedStatement, Object parameterObject) {
		SqlSource nowSqlSource = mappedStatement.getSqlSource();
		SqlNode sqlNode = (SqlNode) ClassUtil.getValue(nowSqlSource, "rootSqlNode");
		DynamicContext context = new DynamicContext(mappedStatement.getConfiguration(), parameterObject);
		sqlNode.apply(context);
		return context.getSql();
	}

	/**
	 * 复制MappedStatement对象，并使用SqlSource来构建复制的新对象
	 * 
	 * @功能描述
	 * @param mappedStatement
	 * @param sqlSource
	 * @return
	 */
	private MappedStatement copyMappedStatementBySqlSource(MappedStatement mappedStatement, SqlSource sqlSource) {
		MappedStatement.Builder builder = new MappedStatement.Builder(mappedStatement.getConfiguration(), mappedStatement.getId(), sqlSource, mappedStatement.getSqlCommandType());
		builder.resource(mappedStatement.getResource());
		builder.fetchSize(mappedStatement.getFetchSize());
		builder.statementType(mappedStatement.getStatementType());
		builder.keyGenerator(mappedStatement.getKeyGenerator());
		builder.keyProperty(ObjectUtil.isNull(mappedStatement.getKeyProperties()) ? null : mappedStatement.getKeyProperties().length > 0 ? mappedStatement.getKeyProperties()[0] : null);
		builder.timeout(mappedStatement.getTimeout());
		builder.parameterMap(mappedStatement.getParameterMap());
		builder.resultMaps(mappedStatement.getResultMaps());
		builder.cache(mappedStatement.getCache());
		builder.useCache(mappedStatement.isUseCache());
		MappedStatement newMappedStatement = builder.build();
		return newMappedStatement;
	}

	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	public void setDialect(Dialect dialect) {
		this.dialect = dialect;
	}

	public void setProperties(Properties properties) {
		String dialectClass = new PropertiesHelper(properties).getRequiredString("dialectClass");
		this.setDialectClass(dialectClass);
	}

	public void setDialectClass(String dialectClass) {
		try {
			this.dialect = (Dialect) Class.forName(dialectClass).newInstance();
		} catch (Exception e) {
			throw new RuntimeException("cannot create dialect instance by dialectClass:" + dialectClass, e);
		}
	}

	public Dialect getDialect() {
		return dialect;
	}
}