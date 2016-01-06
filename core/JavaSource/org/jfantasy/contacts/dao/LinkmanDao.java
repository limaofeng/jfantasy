package org.jfantasy.contacts.dao;

import org.springframework.stereotype.Repository;

import org.jfantasy.contacts.bean.Linkman;
import org.jfantasy.framework.dao.hibernate.HibernateDao;

@Repository("fantasy.ab.hibernate.LinkmanDao")
public class LinkmanDao extends HibernateDao<Linkman, Long>{

}
