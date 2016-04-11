package org.jfantasy.contacts.dao;

import org.jfantasy.contacts.bean.Group;
import org.jfantasy.framework.dao.hibernate.HibernateDao;
import org.springframework.stereotype.Repository;

@Repository("fantasy.ab.hibernate.GroupDao")
public class GroupDao extends HibernateDao<Group, Long> {

}
