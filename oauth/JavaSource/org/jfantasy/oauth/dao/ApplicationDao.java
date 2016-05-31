package org.jfantasy.oauth.dao;


import org.jfantasy.oauth.bean.Application;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository
public class ApplicationDao extends HibernateDao<Application, Long> {
}
