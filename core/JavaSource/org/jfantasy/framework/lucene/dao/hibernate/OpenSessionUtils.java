package org.jfantasy.framework.lucene.dao.hibernate;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate5.SessionFactoryUtils;
import org.springframework.orm.hibernate5.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class OpenSessionUtils {

    private static SessionFactory sessionFactory;

    private static SessionFactory getSessionFactory() {
        return sessionFactory != null ? sessionFactory : (sessionFactory = SpringContextUtil.getBeanByType(SessionFactory.class));
    }

    public static Session openSession() {
        return openSession(getSessionFactory());
    }

    public static Session openSession(SessionFactory sf) {
        try {
            Session session = sessionFactory.openSession();
            session.setFlushMode(FlushMode.MANUAL);
            SessionHolder sessionHolder = new SessionHolder(session);
            TransactionSynchronizationManager.bindResource(sf, sessionHolder);
            return session;
        } catch (HibernateException ex) {
            throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
        }
    }

    public static void closeSession(Session session) {
        SessionFactory sf = session.getSessionFactory();
        SessionHolder sessionHolder = (SessionHolder) TransactionSynchronizationManager.unbindResource(sf);
        SessionFactoryUtils.closeSession(sessionHolder.getSession());
    }

}
