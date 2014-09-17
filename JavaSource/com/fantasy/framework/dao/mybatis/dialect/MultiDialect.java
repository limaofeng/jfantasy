package com.fantasy.framework.dao.mybatis.dialect;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import com.fantasy.framework.dao.MultiDataSourceManager;
import com.fantasy.framework.dao.annotations.DataSource;
import com.fantasy.framework.util.common.ObjectUtil;

/**
 * 多数据源时方言
 * 
 * @功能描述 当使用@DataSource区分数据库时，用于匹配不同的数据源
 * @author 李茂峰
 * @since 2012-10-28 下午08:16:54
 * @version 1.0
 */
public class MultiDialect implements Dialect, InitializingBean {

	private Map<String, Dialect> targetDialects;

	private Map<String, String> dataSourceTypes;

	public void afterPropertiesSet() throws Exception {
		if (ObjectUtil.isNull(targetDialects))
			targetDialects = new HashMap<String, Dialect>();
		targetDialects.put("sql2005", new MsSQLDialect("2005"));
		targetDialects.put("sql2000", new MsSQLDialect("2000"));
		targetDialects.put("db2", new DB2SQLDialect());
		targetDialects.put("oracle", new OraSQLDialect());
		targetDialects.put("mysql", new MySQLDialect());
	}

	public String getCountString(String sql) {
		return getDialect().getCountString(sql);
	}

	private Dialect getDialect() {
		DataSource dataSource = MultiDataSourceManager.getManager().peek();
		Assert.notNull(dataSource, "SqlMapper未指定@DataSource,无法确定翻页方言");
		return targetDialects.get(dataSourceTypes.get(dataSource.name()));
	}

	public String getLimitString(String sql, int offset, int limit) {
		return getDialect().getLimitString(sql, offset, limit);
	}

	public void setTargetDialects(Map<String, Dialect> targetDialects) {
		this.targetDialects = targetDialects;
	}

	public void setDataSourceTypes(Map<String, String> dataSourceTypes) {
		this.dataSourceTypes = dataSourceTypes;
	}

}
