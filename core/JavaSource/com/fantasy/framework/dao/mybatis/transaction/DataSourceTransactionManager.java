package com.fantasy.framework.dao.mybatis.transaction;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;

import javax.sql.DataSource;

public class DataSourceTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, InitializingBean {
	private static final long serialVersionUID = 4259053668416751804L;
	private transient DataSource dataSource;
    private static final Logger LOGGER = Logger.getLogger(DataSourceTransactionManager.class);

	public DataSource getDataSource() {
		return this.dataSource;
	}

	public void afterPropertiesSet() {
		if (getDataSource() == null){
            throw new IllegalArgumentException("Property 'dataSource' is required");
        }
	}

	public Object getResourceFactory() {
		return getDataSource();
	}

	public void setDataSource(DataSource dataSource) {
		if (dataSource instanceof TransactionAwareDataSourceProxy){
            this.dataSource = ((TransactionAwareDataSourceProxy) dataSource).getTargetDataSource();
        }else{
            this.dataSource = dataSource;
        }
	}

	protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
		LOGGER.debug(">doBegin");
	}

	protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
		LOGGER.debug(">doCommit");
	}

	protected Object doGetTransaction() throws TransactionException {
		LOGGER.debug(">doGetTransaction");
		return null;
	}

	protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
	}
}