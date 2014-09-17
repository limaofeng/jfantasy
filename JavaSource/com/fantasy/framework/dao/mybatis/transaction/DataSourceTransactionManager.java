package com.fantasy.framework.dao.mybatis.transaction;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;

public class DataSourceTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {
	private static final long serialVersionUID = 4259053668416751804L;
	private transient DataSource dataSource;

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void afterPropertiesSet() {
		if (getDataSource() == null)
			throw new IllegalArgumentException("Property 'dataSource' is required");
	}

	public Object getResourceFactory() {
		return getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		if ((dataSource instanceof TransactionAwareDataSourceProxy))
			this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
		else
			this.dataSource = dataSource;
	}

	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		System.out.println(">doBegin");
	}

	protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
		System.out.println(">doCommit");
	}

	protected Object doGetTransaction() throws TransactionException {
		System.out.println(">doGetTransaction");
		return null;
	}

	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
	}
}