package org.jfantasy.framework.lucene.dao.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class OpenSessionUtils {

    public static boolean hasResource(SessionFactory sf) {
        return TransactionSynchronizationManager.hasResource(sf);
    }

    public static boolean hasResource() {
        return TransactionSynchronizationManager.hasResource(getSessionFactory());
    }

    public static SessionFactory getSessionFactory(){
        return SpringContextUtil.getBeanByType(SessionFactory.class);
    }

    public static Session openSession() {
        return openSession(getSessionFactory());
    }

    public static Session openSession(SessionFactory sf) {
        try {
            Session session = sf.openSession();
            session.setFlushMode(FlushMode.MANUAL);
            TransactionSynchronizationManager.bindResource(sf, new SessionHolder(session));
            return session;
        } catch (HibernateException ex) {
            throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
        }
    }

    public static void closeSession(Session session) {
        SessionFactory sf = session.getSessionFactory();
        SessionFactoryUtils.closeSession(session);
        TransactionSynchronizationManager.unbindResource(sf);
    }

}
