package com.fantasy.swp.service;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

/**
 * 使用hql语句查询数据
 * Created by wml on 2015/1/30.
 */
@Service
@Transactional
public class HqlService{

    public static final String FIND = "list";

    public static final String FIND_UNIQUE = "object";

    protected SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public SessionFactory getSessionFactory() {
        return this.sessionFactory;
    }

    public HqlService(){
        super();
    }

    public Object execute(String hql,String operate){
        if(FIND.equals(operate)){
            return this.find(hql);
        }else if(FIND_UNIQUE.equals(operate)){
            return this.findUnique(hql);
        }
        return null;
    }

    /**
     * 查询
     * 返回结果集
     * @param hql
     * @return
     */
    public List<Object> find(String hql){
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(hql).list();
    }

    /**
     * 查询唯一结果
     * @param hql
     * @return
     */
    public Object findUnique(String hql){
        Session session = getSessionFactory().getCurrentSession();
        return session.createQuery(hql).uniqueResult();
    }
}
