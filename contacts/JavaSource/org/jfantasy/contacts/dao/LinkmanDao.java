package org.jfantasy.contacts.dao;

import org.jfantasy.contacts.bean.Linkman;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository("fantasy.ab.hibernate.LinkmanDao")
public class LinkmanDao extends HibernateDao<Linkman, Long>{

}
