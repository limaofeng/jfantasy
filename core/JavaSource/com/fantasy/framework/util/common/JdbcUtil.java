package com.fantasy.framework.util.common;

import com.fantasy.framework.spring.SpringContextUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class JdbcUtil extends JdbcUtils {

    private static Log logger = LogFactory.getLog(JdbcUtil.class);

    public static void rollback(Connection con) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public static <T extends Exception> void rollback(Connection con, T re) throws T {
        try {
            con.rollback();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw re;
        }
    }

    public static Transaction transaction() {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED);
        assert transactionManager != null;
        return new Transaction(transactionManager, transactionManager.getTransaction(def));
    }

    public static Transaction transaction(int propagationBehavior) {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(propagationBehavior);
        assert transactionManager != null;
        return new Transaction(transactionManager, transactionManager.getTransaction(def));
    }

    public static <T> T transaction(Callback<T> callback) {
        Transaction transaction = transaction();
        try {
            T val = callback.run();
            if(val instanceof HibernateProxy){
                Hibernate.initialize(val);
            }
            return val;
        } finally {
            transaction.commit();
        }
    }

    public static <T> T transaction(Callback<T> callback,int propagationBehavior) {
        Transaction transaction = transaction(propagationBehavior);
        try {
            T val = callback.run();
            if(val instanceof HibernateProxy){
                Hibernate.initialize(val);
            }
            return val;
        } finally {
            transaction.commit();
        }
    }


    public interface Callback<T> {

        T run();

    }


    public static class Transaction {

        private PlatformTransactionManager transactionManager;
        private TransactionStatus status;

        public Transaction(PlatformTransactionManager transactionManager, TransactionStatus status) {
            this.transactionManager = transactionManager;
            this.status = status;
        }

        public void commit() {
            transactionManager.commit(status);
        }

    }


}
